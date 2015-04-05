package cartesianTree.slpBuilders;

import org.apache.log4j.Logger;

import SLPs.ISLPExtractor;
import SLPs.SlpByteSizeCounter;
import avlTree.slpBuilders.ISLPBuilder;
import cartesianTree.ICartesianTree;
import cartesianTree.ICartesianTreeManager;
import cartesianTree.ICartesianTreeManagerFactory;

import commons.utils.ITimeCounter;
import commons.utils.TimeCounter;

import dataContracts.FactorDef;
import dataContracts.SLPStatistics;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.ICompressionStatistics;

public class CartesianSlpTreeBuilder implements ICartesianSlpTreeBuilder {
    private static Logger logger = Logger.getLogger(CartesianSlpTreeBuilder.class);
    private ICartesianTreeManagerFactory treeManagerFactory;
    private final ISLPExtractor slpExtractor;
    private final SlpByteSizeCounter slpByteSizeCounter;

    public CartesianSlpTreeBuilder(
            ICartesianTreeManagerFactory treeManagerFactory,
            ISLPExtractor slpExtractor,
            SlpByteSizeCounter slpByteSizeCounter) {
        this.treeManagerFactory = treeManagerFactory;
        this.slpExtractor = slpExtractor;
        this.slpByteSizeCounter = slpByteSizeCounter;
    }

    @Override
    public ISLPBuilder buildSlp(FactorDef[] factors, ICompressionStatistics statistics) {
        ITimeCounter timeCounter = new TimeCounter();
        timeCounter.start();
        ICartesianTree resultTree = buildCartesianTree(factors);
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

    private ICartesianTree buildCartesianTree(FactorDef[] factors) {
        ICartesianTreeManager treeManager = treeManagerFactory.create();
        ICartesianTree currentTree = treeManager.getEmptyTree();
        for (int i = 0; i < factors.length; ++i) {
            if (i % 100000 == 0)
                logger.info(String.format("Processed %d factors...", i));
            FactorDef factor = factors[i];
            ICartesianTree newTree;
            if (factor.isTerminal)
                newTree = treeManager.createNewTree((long) factor.symbol);
            else
                newTree = currentTree.substring(factor.beginPosition, factor.beginPosition + factor.length);
            currentTree = currentTree.merge(newTree);
            currentTree.disposeAllButThis();
        }
        return currentTree;
    }
}
