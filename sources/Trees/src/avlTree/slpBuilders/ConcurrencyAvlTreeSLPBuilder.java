package avlTree.slpBuilders;

import java.util.List;

import SLPs.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import avlTree.IAvlTree;
import avlTree.IAvlTreeManager;
import avlTree.IAvlTreeManagerFactory;
import avlTree.helpers.AvlTreeArrayMergeCounter;
import avlTree.helpers.ConcurrentRebalancingCounter;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.treeSets.IAvlTreeSet;
import avlTree.treeSets.IAvlTreeSetFactory;
import commons.utils.ITimeCounter;
import commons.utils.TimeCounter;
import dataContracts.LZFactorDef;
import dataContracts.SLPStatistics;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.ICompressionStatistics;

public class ConcurrencyAvlTreeSLPBuilder implements IConcurrencyAvlTreeSLPBuilder {
	private static final Logger log = LogManager.getLogger(AvlTreeSLPBuilder.class);

    private final IAvlTreeManagerFactory avlTreeManagerFactory;
    private final IAvlTreeSetFactory avlTreeSetFactory;
	private final IParallelExecutorFactory parallelExecutorFactory;
	private final IFactorizationIndexer factorizationIndexer;
    private final ISLPExtractor slpExtractor;
    private final SlpByteSizeCounter slpByteSizeCounter;

    public ConcurrencyAvlTreeSLPBuilder(
            IAvlTreeManagerFactory avlTreeManagerFactory,
            IAvlTreeSetFactory avlTreeSetFactory,
    		IParallelExecutorFactory parallelExecutorFactory,
            IFactorizationIndexer factorizationIndexer,
            ISLPExtractor slpExtractor,
            SlpByteSizeCounter slpByteSizeCounter) {
        this.avlTreeManagerFactory = avlTreeManagerFactory;
        this.avlTreeSetFactory = avlTreeSetFactory;
		this.parallelExecutorFactory = parallelExecutorFactory;
		this.factorizationIndexer = factorizationIndexer;
        this.slpExtractor = slpExtractor;
        this.slpByteSizeCounter = slpByteSizeCounter;
    }


	@Override
	public ISLPBuilder buildSlp(LZFactorDef[] factors, ICompressionStatistics statistics)
    {
        return buildSlp(factors, statistics, new ConcurrentAvlBuilderStopwatches());
    }

	@Override
	public ISLPBuilder buildSlp(LZFactorDef[] factors, ICompressionStatistics statistics, ConcurrentAvlBuilderStopwatches stopwatches)
	{
        ITimeCounter timeCounter = new TimeCounter();
        timeCounter.start();

        stopwatches.totalStopwatch.start();
        IAvlTree resultTree = buildAvlTree(factors, statistics, stopwatches);

        stopwatches.minimizeTreeStopwatch.start();
        ISLPBuilder slp = slpExtractor.getSLP(resultTree);
        stopwatches.minimizeTreeStopwatch.stop();
        stopwatches.totalStopwatch.stop();
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

    private IAvlTree buildAvlTree(LZFactorDef[] factors, ICompressionStatistics statistics, ConcurrentAvlBuilderStopwatches stopwatches) {
        IRebalancingCounter rebalanceCounter = new ConcurrentRebalancingCounter();
        IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter = new AvlTreeArrayMergeCounter();

        final IAvlTreeManager avlTreeManager = avlTreeManagerFactory.create();
        final IAvlTreeSet avlTreeSet = avlTreeSetFactory.create(parallelExecutorFactory, avlTreeManager, rebalanceCounter, avlTreeArrayMergeCounter);
        stopwatches.findingLayersStopwatch.start();
        List<List<Integer>> layers = factorizationIndexer.index(factors);
        stopwatches.findingLayersStopwatch.stop();

        int layersNumber = layers.size();
        for (int level = 0; level < layersNumber; ++level) {
        	buildTreeLevel(avlTreeSet, factors, layers.get(level), stopwatches);
            log.info(String.format("Processed %d from %d layers. There are %d factors on this layers.", level + 1, layersNumber, layers.get(level).size()));
        }
        IAvlTree resultTree = avlTreeSet.getSingleTree();

        avlTreeArrayMergeCounter.printStatistics();
        statistics.putParam(CompressionStatisticKeys.RebalanceCount, rebalanceCounter.getCount());
        statistics.putParam(CompressionStatisticKeys.CountOfLayers, layersNumber);
        return resultTree;
    }

    private void buildTreeLevel(final IAvlTreeSet avlTreeSet, LZFactorDef[] factors, List<Integer> levelIndexes, ConcurrentAvlBuilderStopwatches stopwatches) {
		long starMs = System.currentTimeMillis();

        stopwatches.processingLayersStopwatch.start();
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
        stopwatches.waitProcessingLayersStopwatch.start();
        parallelExecutor.await();
        stopwatches.waitProcessingLayersStopwatch.stop();
        stopwatches.processingLayersStopwatch.stop();

        avlTreeSet.mergeNeighboringTree(stopwatches);

        long endMs = System.currentTimeMillis();
        log.info(String.format("buildTreeLevel %d ms.", endMs - starMs));
	}
}
