package avlTree.buffers;

import avlTree.IAvlTree;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.INodesCacheStatisticsCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.mergers.IAvlTreeArrayMerger;

public class AvlTreeSplitMergedBuffer extends AvlTreeBuffer {

    public AvlTreeSplitMergedBuffer(
            INodesCacheStatisticsCounter nodesCacheStatisticsCounter,
            IAvlTreeArrayMerger avlTreeArrayMerger,
            IRebalancingCounter rebalancingCounter,
            IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter,
            IAvlTree avlTree) {
        super(nodesCacheStatisticsCounter, avlTreeArrayMerger, rebalancingCounter, avlTreeArrayMergeCounter, avlTree);
    }

    @Override
    public IAvlTree substring(long leftInclusive, long rightExclusive) {
        mergeAll();
        return treeList.get(0).split(leftInclusive, rightExclusive, rebalancingCounter);
    }
}
