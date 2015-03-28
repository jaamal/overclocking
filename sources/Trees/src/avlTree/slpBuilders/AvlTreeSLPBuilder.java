package avlTree.slpBuilders;

import SLPs.ISLPExtractor;
import SLPs.SlpByteSizeCounter;
import avlTree.IAvlTree;
import avlTree.IAvlTreeManager;
import avlTree.IAvlTreeManagerFactory;
import avlTree.buffers.IAvlTreeBuffer;
import avlTree.buffers.IAvlTreeBufferFactory;
import avlTree.helpers.*;
import commons.utils.ITimeCounter;
import commons.utils.TimeCounter;
import dataContracts.LZFactorDef;
import dataContracts.SLPStatistics;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.ICompressionStatistics;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AvlTreeSLPBuilder implements IAvlTreeSLPBuilder {
    private static final Logger log = LogManager.getLogger(AvlTreeSLPBuilder.class);

    private IAvlTreeManagerFactory avlTreeManagerFactory;
    private IAvlTreeBufferFactory avlTreeBufferFactory;
    private final ISLPExtractor slpExtractor;
    private final SlpByteSizeCounter slpByteSizeCounter;

    public AvlTreeSLPBuilder(
            IAvlTreeManagerFactory avlTreeManagerFactory,
            IAvlTreeBufferFactory avlTreeBufferFactory,
            ISLPExtractor slpExtractor,
            SlpByteSizeCounter slpByteSizeCounter) {
        this.avlTreeManagerFactory = avlTreeManagerFactory;
        this.avlTreeBufferFactory = avlTreeBufferFactory;
        this.slpExtractor = slpExtractor;
        this.slpByteSizeCounter = slpByteSizeCounter;
    }

    @Override
    public ISLPBuilder buildSlp(LZFactorDef[] factors, ICompressionStatistics statistics) {
        ITimeCounter timeCounter = new TimeCounter();
        timeCounter.start();
        IAvlTree resultTree = buildAvlTree(factors, statistics);
        ISLPBuilder slp = slpExtractor.getSLP(resultTree);
        timeCounter.end();
        resultTree.dispose();

        statistics.putParam(CompressionStatisticKeys.FactorizationLength, factors.length);
        statistics.putParam(CompressionStatisticKeys.RunningTime, timeCounter.getTime());

        SLPStatistics slpStatistics = slp.getStatistics();
        statistics.putParam(CompressionStatisticKeys.SourceLength, slpStatistics.length);
        statistics.putParam(CompressionStatisticKeys.SlpHeight, slpStatistics.height);
        statistics.putParam(CompressionStatisticKeys.SlpCountRules, slpStatistics.countRules);
        statistics.putParam(CompressionStatisticKeys.SlpByteSize, slpByteSizeCounter.getSlpByteSize(slp));

        return slp;
    }

    private IAvlTree buildAvlTree(LZFactorDef[] factors, ICompressionStatistics statistics) {
        NodesCacheStatisticsCounter nodesCacheStatisticsCounter = new NodesCacheStatisticsCounter();
        IRebalancingCounter rebalancingCounter = new RebalancingCounter();
        IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter = new AvlTreeArrayMergeCounter();

        IAvlTreeManager avlTreeManager = avlTreeManagerFactory.create();
        IAvlTreeBuffer buffer = avlTreeBufferFactory.create(rebalancingCounter, nodesCacheStatisticsCounter, avlTreeArrayMergeCounter, avlTreeManager.createEmptyTree());
        for (int i = 0; i < factors.length; i++) {
            LZFactorDef factor = factors[i];
            if (factor.isTerminal)
                buffer.append(avlTreeManager.createNewTree((long) factor.symbol));
            else
                buffer.append(buffer.substring(factor.beginPosition, factor.beginPosition + factor.length));

            if (i % 10000 == 0) {
                log.info(String.format("Processed %d factors from %d", i, factors.length));
            }
        }
        IAvlTree resultTree = buffer.getTree();

        avlTreeArrayMergeCounter.printStatistics();
        statistics.putParam(CompressionStatisticKeys.RebalanceCount, rebalancingCounter.getCount());
        return resultTree;
    }
}

