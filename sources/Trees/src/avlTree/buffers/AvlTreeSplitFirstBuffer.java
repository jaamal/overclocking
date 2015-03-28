package avlTree.buffers;

import avlTree.IAvlTree;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.INodesCacheStatisticsCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.mergers.IAvlTreeArrayMerger;

public class AvlTreeSplitFirstBuffer extends AvlTreeBuffer {

    public AvlTreeSplitFirstBuffer(
            INodesCacheStatisticsCounter nodesCacheStatisticsCounter,
            IAvlTreeArrayMerger avlTreeArrayMerger,
            IRebalancingCounter rebalancingCounter,
            IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter,
            IAvlTree avlTree) {
        super(nodesCacheStatisticsCounter, avlTreeArrayMerger, rebalancingCounter, avlTreeArrayMergeCounter, avlTree);
    }

    @Override
    public IAvlTree substring(long leftInclusive, long rightExclusive) {
        if (rightExclusive > treeList.get(0).getWidth())
            mergeAll();
        return treeList.get(0).split(leftInclusive, rightExclusive, rebalancingCounter);
    }
}


