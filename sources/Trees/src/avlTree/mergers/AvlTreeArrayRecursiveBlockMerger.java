package avlTree.mergers;

import avlTree.IAvlTree;
import avlTree.helpers.IRebalancingCounter;

public class AvlTreeArrayRecursiveBlockMerger extends AvlTreeArrayMergerWithStatisticsCollecting  {

    @Override
    protected IAvlTree merge(IAvlTree[] trees, IRebalancingCounter rebalancingCounter) {
        final int blockLength = 31;
        while (trees.length != 1) {
            IAvlTree[] blocks = new IAvlTree[(trees.length + blockLength - 1) / blockLength];
            for (int j = 0; j < blocks.length; ++j) {
                int offset = j * blockLength;
                int count = Math.min(trees.length - offset, blockLength);
                blocks[j] = new MergingStrategy(trees, offset, count).apply(rebalancingCounter);
            }
            trees = blocks;
        }
        return trees[0];
    }
}

