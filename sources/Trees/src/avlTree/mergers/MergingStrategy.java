package avlTree.mergers;

import avlTree.IAvlTree;
import avlTree.helpers.IRebalancingCounter;

public class MergingStrategy implements IMergingStrategy {
    private final IAvlTree[] avlTrees;
    private final int offset;
    private final int count;

    private final int[][] bestValues;
    private final int[][] bestIndexes;
    private final int[][] heights;

    public MergingStrategy(IAvlTree[] avlTrees) {
        this(avlTrees, 0, avlTrees.length);
    }

    public MergingStrategy(IAvlTree[] avlTrees, int offset, int count) {
        this.avlTrees = avlTrees;
        this.offset = offset;
        this.count = count;

        bestValues = new int[count][count];
        bestIndexes = new int[count][count];
        heights = new int[count][count];
        for (int i = 0; i < count; i++)
            for (int j = 0; j < count; j++)
                bestValues[i][j] = -1;
    }

    @Override
    public IAvlTree apply(IRebalancingCounter rebalancingCounter) {
        return apply(0, count - 1, rebalancingCounter);
    }

    private IAvlTree apply(int leftInclusive, int rightInclusive, IRebalancingCounter rebalancingCounter) {
        if (leftInclusive == rightInclusive)
            return getTree(leftInclusive);
        int middle = calculateBestIndex(leftInclusive, rightInclusive);
        IAvlTree leftTree = apply(leftInclusive, middle, rebalancingCounter);
        IAvlTree rightTree = apply(middle + 1, rightInclusive, rebalancingCounter);
        return leftTree.merge(rightTree, rebalancingCounter);
    }

    private IAvlTree getTree(int index) {
        return avlTrees[index + offset];
    }

    private int getHeight(int leftInclusive, int rightInclusive) {
        int maxHeight = Integer.MIN_VALUE;
        for (int i = leftInclusive; i <= rightInclusive; i++)
            maxHeight = Math.max(maxHeight, (int) getTree(i).getHeight());
        return maxHeight + rightInclusive - leftInclusive;
    }

    private int calculateBestIndex(int leftInclusive, int rightInclusive) {
        if (leftInclusive > rightInclusive)
            throw new RuntimeException(String.format("Invalid range [%d, %d]", leftInclusive, rightInclusive));

        if (bestValues[leftInclusive][rightInclusive] != -1)
            return bestIndexes[leftInclusive][rightInclusive];

        if (leftInclusive == rightInclusive) {
            bestValues[leftInclusive][rightInclusive] = 0;
            bestIndexes[leftInclusive][rightInclusive] = -1;
            heights[leftInclusive][rightInclusive] = (int) getTree(leftInclusive).getHeight();
        } else
            calculateValue(leftInclusive, rightInclusive);

        return bestIndexes[leftInclusive][rightInclusive];
    }

    private void calculateValue(int leftInclusive, int rightInclusive) {
        int bestValue = Integer.MAX_VALUE;
        int bestIndex = -1;
        for (int index = leftInclusive; index < rightInclusive; index++) {
            calculateBestIndex(leftInclusive, index);
            calculateBestIndex(index + 1, rightInclusive);
            int candidate = bestValues[leftInclusive][index] + bestValues[index + 1][rightInclusive] +
                    Math.abs(heights[leftInclusive][index] - heights[index + 1][rightInclusive]);
            if (candidate < bestValue) {
                bestValue = candidate;
                bestIndex = index;
            }
        }
        bestValues[leftInclusive][rightInclusive] = bestValue;
        bestIndexes[leftInclusive][rightInclusive] = bestIndex;
        heights[leftInclusive][rightInclusive] = getHeight(leftInclusive, rightInclusive);
    }
}
