package avlTree.treeSets;

import avlTree.IAvlTree;
import avlTree.IAvlTreeManager;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.mergers.IAvlTreeArrayMerger;
import avlTree.slpBuilders.ConcurrentAvlBuilderStopwatches;
import avlTree.slpBuilders.IParallelExecutor;
import avlTree.slpBuilders.IParallelExecutorFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvlTreeSet implements IAvlTreeSet {
    private PositionedAvlTree[] trees;
    private final List<PositionedAvlTree> newTrees;
    private final IAvlTreeManager avlTreeManager;
    private final IAvlTreeArrayMerger avlTreeArrayMerger;
    private final IRebalancingCounter rebalancingCounter;
    private final IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter;
    private final IParallelExecutorFactory parallelExecutorFactory;

    public AvlTreeSet(
            IParallelExecutorFactory parallelExecutorFactory,
            IAvlTreeManager avlTreeManager,
            IAvlTreeArrayMerger avlTreeArrayMerger,
            IRebalancingCounter rebalancingCounter,
            IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter) {
        this.parallelExecutorFactory = parallelExecutorFactory;
        this.avlTreeManager = avlTreeManager;
        this.avlTreeArrayMerger = avlTreeArrayMerger;
        this.rebalancingCounter = rebalancingCounter;
        this.avlTreeArrayMergeCounter = avlTreeArrayMergeCounter;
        trees = new PositionedAvlTree[0];
        newTrees = new ArrayList<>();
    }

    @Override
    public void addSymbol(long offset, long symbol) {
        addTree(offset, avlTreeManager.createNewTree(symbol));
    }

    @Override
    public void addSubstring(long offset, long beginPosition, long length) {
        int left = 0;
        int right = trees.length;
        while (left + 1 < right) {
            int middle = (left + right + 1) / 2;
            if (trees[middle].offset <= beginPosition)
                left = middle;
            else
                right = middle;
        }
        PositionedAvlTree bigTree = trees[left];
        IAvlTree substringTree = bigTree.tree.split(beginPosition - bigTree.offset, beginPosition + length - bigTree.offset, rebalancingCounter);
        addTree(offset, substringTree);
    }

    private void addTree(long offset, IAvlTree newFactorTree) {
        synchronized (newTrees) {
            newTrees.add(new PositionedAvlTree(newFactorTree, offset));
        }
    }

    @Override
    public void mergeNeighboringTree(ConcurrentAvlBuilderStopwatches stopwatches) {
        stopwatches.mergeNeighbouringTreesStopwatch.start();
        synchronized (newTrees) {
            PositionedAvlTree[] newTreesArray = newTrees.toArray(new PositionedAvlTree[0]);
            newTrees.clear();

            final PositionedAvlTree[] allTrees = new PositionedAvlTree[newTreesArray.length + trees.length];
            for (int i = 0; i < allTrees.length; ++i) {
                if (i < trees.length)
                    allTrees[i] = trees[i];
                else
                    allTrees[i] = newTreesArray[i - trees.length];
            }
            Arrays.sort(allTrees);

            IParallelExecutor parallelExecutor = parallelExecutorFactory.create();
            final ArrayList<PositionedAvlTree> mergedTrees = new ArrayList<>();
            for (int left = 0; left < allTrees.length; ) {
                int right = left + 1;
                while (right < allTrees.length && allTrees[right - 1].offset + allTrees[right - 1].tree.getWidth() == allTrees[right].offset) {
                    ++right;
                }
                if (left + 1 == right) {
                    mergedTrees.add(allTrees[left]);
                } else {
                    final PositionedAvlTree currentTree = new PositionedAvlTree(null, allTrees[left].offset);
                    final int localLeft = left;
                    final int localRight = right;
                    mergedTrees.add(currentTree);
                    parallelExecutor.append(new Runnable() {
                        @Override
                        public void run() {
                            IAvlTree[] array = new IAvlTree[localRight - localLeft];
                            for (int i = localLeft; i < localRight; ++i)
                                array[i - localLeft] = allTrees[i].tree;
                            currentTree.tree = avlTreeArrayMerger.merge(array, rebalancingCounter, avlTreeArrayMergeCounter);
                        }
                    });
                }
                left = right;
            }
            stopwatches.waitMergeNeighbouringTreesStopwatch.start();
            parallelExecutor.await();
            stopwatches.waitMergeNeighbouringTreesStopwatch.stop();
            trees = mergedTrees.toArray(new PositionedAvlTree[mergedTrees.size()]);

            long[] numbers = new long[trees.length];
            for (int index = 0; index < trees.length; ++index)
                numbers[index] = trees[index].tree.getRoot().number;
            avlTreeManager.disposeAllBut(numbers);
        }
        stopwatches.mergeNeighbouringTreesStopwatch.stop();
    }

    @Override
    public IAvlTree getSingleTree() {
        synchronized (newTrees) {
            if (newTrees.size() != 0)
                throw new RuntimeException("Can not call method 'getSingleTree' without pre-call method 'mergeNeighboringTree'");
            if (trees.length > 1)
                throw new RuntimeException("There are several trees in set");
            return trees.length == 0 ? null : trees[0].tree;
        }
    }

    private class PositionedAvlTree implements Comparable<PositionedAvlTree> {
        private PositionedAvlTree(IAvlTree tree, long offset) {
            this.tree = tree;
            this.offset = offset;
        }

        public IAvlTree tree;
        public final long offset;

        @Override
        public int compareTo(PositionedAvlTree other) {
            if (offset < other.offset)
                return -1;
            if (offset > other.offset)
                return 1;
            return 0;
        }
    }
}
