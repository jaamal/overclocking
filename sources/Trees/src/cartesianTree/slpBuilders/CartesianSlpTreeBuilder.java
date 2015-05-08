package cartesianTree.slpBuilders;

import org.apache.log4j.Logger;

import serialization.products.IProductsSerializer;
import SLPs.ISLPExtractor;
import avlTree.slpBuilders.ISLPBuilder;
import cartesianTree.ICartesianTree;
import cartesianTree.ICartesianTreeManager;
import cartesianTree.ICartesianTreeManagerFactory;

import commons.utils.TimeCounter;

import dataContracts.FactorDef;
import dataContracts.SLPModel;
import dataContracts.SLPStatistics;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.StatisticKeys;

public class CartesianSlpTreeBuilder implements ICartesianSlpTreeBuilder {
    private static Logger logger = Logger.getLogger(CartesianSlpTreeBuilder.class);
    private ICartesianTreeManagerFactory treeManagerFactory;
    private final ISLPExtractor slpExtractor;
    private final IProductsSerializer productsSerializer;

    public CartesianSlpTreeBuilder(
            ICartesianTreeManagerFactory treeManagerFactory,
            ISLPExtractor slpExtractor,
            IProductsSerializer productsSerializer) {
        this.treeManagerFactory = treeManagerFactory;
        this.slpExtractor = slpExtractor;
        this.productsSerializer = productsSerializer;
    }

    @Override
    public SLPModel buildSlp(FactorDef[] factors, IStatistics statistics) {
        TimeCounter timeCounter = TimeCounter.start();
        ICartesianTree resultTree = buildCartesianTree(factors);
        ISLPBuilder slpBuilder = slpExtractor.getSLP(resultTree);
        timeCounter.finish();
        resultTree.dispose();

        SLPModel slpModel = slpBuilder.toSLPModel();
        statistics.putParam(StatisticKeys.FactorizationLength, factors.length);
        statistics.putParam(StatisticKeys.RunningTime, timeCounter.getMillis());

        SLPStatistics slpStatistics = slpModel.calcStats();
        statistics.putParam(StatisticKeys.SourceLength, slpStatistics.length);
        slpModel.appendStats(statistics, productsSerializer);
        return slpModel;
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
