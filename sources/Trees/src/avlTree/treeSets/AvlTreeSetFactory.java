package avlTree.treeSets;

import avlTree.IAvlTreeManager;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.mergers.IAvlTreeArrayMerger;
import avlTree.slpBuilders.IParallelExecutorFactory;

public class AvlTreeSetFactory implements IAvlTreeSetFactory {
    private final IAvlTreeArrayMerger avlTreeArrayMerger;

    public AvlTreeSetFactory(IAvlTreeArrayMerger avlTreeArrayMerger) {
        this.avlTreeArrayMerger = avlTreeArrayMerger;
    }

    @Override
    public IAvlTreeSet create(IParallelExecutorFactory parallelExecutorFactory, IAvlTreeManager avlTreeManager, IRebalancingCounter rebalancingCounter, IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter) {
        return new AvlTreeSet(parallelExecutorFactory, avlTreeManager, avlTreeArrayMerger, rebalancingCounter, avlTreeArrayMergeCounter);
    }
}
