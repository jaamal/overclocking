package avlTree.slpBuilders;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import SLPs.ISLPExtractor;
import SLPs.SlpByteSizeCounter;
import avlTree.IAvlTree;
import avlTree.IAvlTreeManager;
import avlTree.IAvlTreeManagerFactory;
import avlTree.buffers.IAvlTreeBuffer;
import avlTree.buffers.IAvlTreeBufferFactory;
import avlTree.helpers.AvlTreeArrayMergeCounter;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.helpers.NodesCacheStatisticsCounter;
import avlTree.helpers.RebalancingCounter;

import commons.utils.TimeCounter;

import dataContracts.FactorDef;
import dataContracts.SLPStatistics;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.IStatistics;

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
    public ISLPBuilder buildSlp(FactorDef[] factors, IStatistics statistics) {
        TimeCounter timeCounter = TimeCounter.start();
        IAvlTree resultTree = buildAvlTree(factors, statistics);
        ISLPBuilder slp = slpExtractor.getSLP(resultTree);
        timeCounter.finish();
        resultTree.dispose();

        statistics.putParam(StatisticKeys.FactorizationLength, factors.length);
        statistics.putParam(StatisticKeys.RunningTime, timeCounter.getMillis());

        SLPStatistics slpStatistics = slp.getStatistics();
        statistics.putParam(StatisticKeys.SourceLength, slpStatistics.length);
        statistics.putParam(StatisticKeys.SlpHeight, slpStatistics.height);
        statistics.putParam(StatisticKeys.SlpCountRules, slpStatistics.countRules);
        statistics.putParam(StatisticKeys.SlpByteSize, slpByteSizeCounter.getSlpByteSize(slp));
        return slp;
    }
    
    private IAvlTree buildAvlTree(FactorDef[] factors, IStatistics statistics) {
        NodesCacheStatisticsCounter nodesCacheStatisticsCounter = new NodesCacheStatisticsCounter();
        IRebalancingCounter rebalancingCounter = new RebalancingCounter();
        IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter = new AvlTreeArrayMergeCounter();

        IAvlTreeManager avlTreeManager = avlTreeManagerFactory.create();
        IAvlTreeBuffer buffer = avlTreeBufferFactory.create(rebalancingCounter, nodesCacheStatisticsCounter, avlTreeArrayMergeCounter, avlTreeManager.createEmptyTree());
        for (int i = 0; i < factors.length; i++) {
            FactorDef factor = factors[i];
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
        statistics.putParam(StatisticKeys.RebalanceCount, rebalancingCounter.getCount());
        return resultTree;
    }
}

