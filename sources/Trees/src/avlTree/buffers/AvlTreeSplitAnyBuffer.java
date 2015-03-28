package avlTree.buffers;

import avlTree.IAvlTree;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.INodesCacheStatisticsCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.mergers.IAvlTreeArrayMerger;

public class AvlTreeSplitAnyBuffer extends AvlTreeBuffer {

    public AvlTreeSplitAnyBuffer(
            INodesCacheStatisticsCounter nodesCacheStatisticsCounter,
            IAvlTreeArrayMerger avlTreeArrayMerger,
            IRebalancingCounter rebalancingCounter,
            IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter,
            IAvlTree avlTree) {
        super(nodesCacheStatisticsCounter, avlTreeArrayMerger, rebalancingCounter, avlTreeArrayMergeCounter, avlTree);
    }

    @Override
    public IAvlTree substring(long leftInclusive, long rightExclusive) {
        long skip = 0;
        for (IAvlTree avlTree : treeList) {
            if (skip + avlTree.getWidth() < rightExclusive)
                skip += avlTree.getWidth();
            else {
                if (skip <= leftInclusive)
                    return avlTree.split(leftInclusive - skip, rightExclusive - skip, rebalancingCounter);
                break;
            }
        }
        mergeAll();
        return treeList.get(0).split(leftInclusive, rightExclusive, rebalancingCounter);
    }
}
