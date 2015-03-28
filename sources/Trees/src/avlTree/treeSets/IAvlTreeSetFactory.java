package avlTree.treeSets;

import avlTree.IAvlTreeManager;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.slpBuilders.IParallelExecutorFactory;

public interface IAvlTreeSetFactory {
    IAvlTreeSet create(IParallelExecutorFactory parallelExecutorFactory, IAvlTreeManager avlTreeManager, IRebalancingCounter rebalancingCounter, IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter);
}
