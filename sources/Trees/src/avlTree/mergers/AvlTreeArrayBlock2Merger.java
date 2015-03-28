package avlTree.mergers;

import avlTree.IAvlTree;
import avlTree.helpers.IRebalancingCounter;

public class AvlTreeArrayBlock2Merger extends AvlTreeArrayMergerWithStatisticsCollecting {
    private AvlTreeArraySequentialMerger avlTreeArraySequentialMerger;

    public AvlTreeArrayBlock2Merger(AvlTreeArraySequentialMerger avlTreeArraySequentialMerger) {
        this.avlTreeArraySequentialMerger = avlTreeArraySequentialMerger;
    }

    @Override
    protected IAvlTree merge(IAvlTree[] trees, IRebalancingCounter rebalancingCounter) {
        if (trees.length > 10)
            trees = mergeInternal(trees, rebalancingCounter, 10);
        trees = mergeInternal(trees, rebalancingCounter, 10);
        return avlTreeArraySequentialMerger.merge(trees, rebalancingCounter);
    }

    private IAvlTree[] mergeInternal(IAvlTree[] trees, IRebalancingCounter rebalancingCounter, final int blockLength) {
        if (trees.length == 1 || blockLength == 1)
            return trees;
        IAvlTree[] blocks = new IAvlTree[(trees.length + blockLength - 1) / blockLength];
        for (int j = 0; j < blocks.length; ++j) {
            int offset = j * blockLength;
            int count = Math.min(trees.length - offset, blockLength);
            blocks[j] = new MergingStrategy(trees, offset, count).apply(rebalancingCounter);
        }
        return blocks;
    }
}
