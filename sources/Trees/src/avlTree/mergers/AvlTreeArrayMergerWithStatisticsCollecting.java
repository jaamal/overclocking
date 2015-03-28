package avlTree.mergers;

import avlTree.IAvlTree;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.IRebalancingCounter;

public abstract class AvlTreeArrayMergerWithStatisticsCollecting implements IAvlTreeArrayMerger {

    @Override
    public IAvlTree merge(IAvlTree[] trees, IRebalancingCounter rebalancingCounter, IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter) {
        if (trees.length == 1)
            return trees[0];
        avlTreeArrayMergeCounter.fixMerge(trees.length);
        return merge(trees, rebalancingCounter);
    }

    protected abstract IAvlTree merge(IAvlTree[] trees, IRebalancingCounter rebalancingCounter);
}
