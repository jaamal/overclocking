package avlTree.slpBuilders;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import serialization.products.IProductsSerializer;
import SLPs.ISLPExtractor;
import avlTree.IAvlTree;
import avlTree.IAvlTreeManager;
import avlTree.IAvlTreeManagerFactory;
import avlTree.helpers.AvlTreeArrayMergeCounter;
import avlTree.helpers.ConcurrentRebalancingCounter;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.treeSets.IAvlTreeSet;
import avlTree.treeSets.IAvlTreeSetFactory;

import commons.utils.TimeCounter;

import dataContracts.FactorDef;
import dataContracts.LZFactorDef;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.StatisticKeys;

public class ConcurrencyAvlTreeSLPBuilder implements IConcurrencyAvlTreeSLPBuilder {
	private static final Logger log = LogManager.getLogger(AvlTreeSLPBuilder.class);

    private final IAvlTreeManagerFactory avlTreeManagerFactory;
    private final IAvlTreeSetFactory avlTreeSetFactory;
	private final IParallelExecutorFactory parallelExecutorFactory;
	private final IFactorizationIndexer factorizationIndexer;
    private final ISLPExtractor slpExtractor;
    private IProductsSerializer productsSerializer;

    public ConcurrencyAvlTreeSLPBuilder(
            IAvlTreeManagerFactory avlTreeManagerFactory,
            IAvlTreeSetFactory avlTreeSetFactory,
    		IParallelExecutorFactory parallelExecutorFactory,
            IFactorizationIndexer factorizationIndexer,
            ISLPExtractor slpExtractor,
            IProductsSerializer productsSerializer) {
        this.avlTreeManagerFactory = avlTreeManagerFactory;
        this.avlTreeSetFactory = avlTreeSetFactory;
		this.parallelExecutorFactory = parallelExecutorFactory;
		this.factorizationIndexer = factorizationIndexer;
        this.slpExtractor = slpExtractor;
        this.productsSerializer = productsSerializer;
    }

	@Override
	public SLPModel buildSlp(FactorDef[] factors, IStatistics statistics)
	{
	    LZFactorDef[] clonedFactorization = cloneFactorization(factors);
	    
        TimeCounter algorithmsTimeCounter = TimeCounter.start();
        IAvlTree resultTree = buildAvlTree(clonedFactorization, statistics);
        TimeCounter minimizationTimeCounter = TimeCounter.start();
        ISLPBuilder slpBuilder = slpExtractor.getSLP(resultTree);
        log.info(String.format("Slp minimization finished during %d ms.", minimizationTimeCounter.finish().toMillis()));
        log.info(String.format("Algorithm finished during %d ms.", algorithmsTimeCounter.finish().toMillis()));
        resultTree.dispose();
        
        SLPModel slpModel = slpBuilder.toSLPModel();
        statistics.putParam(StatisticKeys.FactorizationLength, factors.length);
        statistics.putParam(StatisticKeys.RunningTime, algorithmsTimeCounter.getMillis());

        slpModel.appendStats(statistics, productsSerializer);
        return slpModel;
	}
	
	//TODO: cheat, we should found the way to store factors and its offest's separately.
	private LZFactorDef[] cloneFactorization(FactorDef[] factors) {
	    LZFactorDef[] result = new LZFactorDef[factors.length];
	    for (int i = 0; i < factors.length; i++) {
	        result[i] = factors[i].isTerminal 
	                ? new LZFactorDef((char)factors[i].symbol)
	                : new LZFactorDef(factors[i].beginPosition, factors[i].length);
	    }
	    return result;
	}

    private IAvlTree buildAvlTree(LZFactorDef[] factors, IStatistics statistics) {
        IRebalancingCounter rebalanceCounter = new ConcurrentRebalancingCounter();
        IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter = new AvlTreeArrayMergeCounter();

        final IAvlTreeManager avlTreeManager = avlTreeManagerFactory.create();
        final IAvlTreeSet avlTreeSet = avlTreeSetFactory.create(parallelExecutorFactory, avlTreeManager, rebalanceCounter, avlTreeArrayMergeCounter);
        TimeCounter timeCounter = TimeCounter.start();
        List<List<Integer>> layers = factorizationIndexer.index(factors);
        log.info(String.format("Layers indexed during %d ms.", timeCounter.finish().toMillis()));

        int layersNumber = layers.size();
        for (int level = 0; level < layersNumber; ++level) {
            buildTreeLevel(avlTreeSet, factors, layers.get(level));
            log.info(String.format("Processed %d from %d layers. There are %d factors on this layers.", level + 1, layersNumber, layers.get(level).size()));
        }
        IAvlTree resultTree = avlTreeSet.getSingleTree();

        avlTreeArrayMergeCounter.printStatistics();
        statistics.putParam(StatisticKeys.RebalanceCount, rebalanceCounter.getCount());
        statistics.putParam(StatisticKeys.CountOfLayers, layersNumber);
        return resultTree;
    }

    private void buildTreeLevel(final IAvlTreeSet avlTreeSet, LZFactorDef[] factors, List<Integer> levelIndexes) {
        TimeCounter timeCounter = TimeCounter.start();

        IParallelExecutor parallelExecutor = parallelExecutorFactory.create();
		for (Integer levelIndex : levelIndexes) {
			final LZFactorDef factor = factors[levelIndex];
            parallelExecutor.append(new Runnable() {
				@Override
				public void run() {
					if (factor.isTerminal)
                        avlTreeSet.addSymbol(factor.offset, factor.symbol);
                    else
                        avlTreeSet.addSubstring(factor.offset, factor.beginPosition, factor.length);
				}
			});
		}
        parallelExecutor.await();
        log.info(String.format("Tree layer processed during %d ms.", timeCounter.getMillis()));

        avlTreeSet.mergeNeighboringTree();
        log.info(String.format("Level tree builded during %d ms.", timeCounter.finish().toMillis()));
	}
}
