package avlTree.mergers;

import avlTree.IAvlTree;
import avlTree.helpers.IRebalancingCounter;

public class AvlTreeArrayBlockMerger extends AvlTreeArrayMergerWithStatisticsCollecting {
    private AvlTreeArraySequentialMerger avlTreeArraySequentialMerger;

    public AvlTreeArrayBlockMerger(AvlTreeArraySequentialMerger avlTreeArraySequentialMerger) {
        this.avlTreeArraySequentialMerger = avlTreeArraySequentialMerger;
    }

    @Override
    protected IAvlTree merge(IAvlTree[] trees, IRebalancingCounter rebalancingCounter) {
        final int blockLength = 10;
        IAvlTree[] blocks = new IAvlTree[(trees.length + blockLength - 1) / blockLength];
        for (int j = 0; j < blocks.length; ++j)
        {
            int offset = j * blockLength;
            int count = Math.min(trees.length - offset, blockLength);
            blocks[j] = new MergingStrategy(trees, offset, count).apply(rebalancingCounter);
        }
        return avlTreeArraySequentialMerger.merge(blocks, rebalancingCounter);
    }
}

