package avlTree.mergers;

import avlTree.IAvlTree;
import avlTree.helpers.IRebalancingCounter;

public class AvlTreeArraySequentialMerger  extends AvlTreeArrayMergerWithStatisticsCollecting  {
    @Override
    protected IAvlTree merge(IAvlTree[] trees, IRebalancingCounter rebalancingCounter) {
        IAvlTree result = trees[0];
        for (int i = 1; i < trees.length; ++i)
            result = result.merge(trees[i], rebalancingCounter);
        return result;
    }
}
