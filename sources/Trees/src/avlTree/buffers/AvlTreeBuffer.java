package avlTree.buffers;

import java.util.ArrayList;

import avlTree.IAvlTree;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.INodesCacheStatisticsCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.mergers.IAvlTreeArrayMerger;

public abstract class AvlTreeBuffer implements IAvlTreeBuffer {

    protected ArrayList<IAvlTree> treeList;
    protected final IRebalancingCounter rebalancingCounter;
    private INodesCacheStatisticsCounter nodesCacheStatisticsCounter;
    private final IAvlTreeArrayMerger avlTreeArrayMerger;
    private IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter;

    protected AvlTreeBuffer(
            INodesCacheStatisticsCounter nodesCacheStatisticsCounter,
            IAvlTreeArrayMerger avlTreeArrayMerger,
            IRebalancingCounter rebalancingCounter,
            IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter,
            IAvlTree avlTree) {
        this.nodesCacheStatisticsCounter = nodesCacheStatisticsCounter;
        this.avlTreeArrayMerger = avlTreeArrayMerger;
        this.rebalancingCounter = rebalancingCounter;
        this.avlTreeArrayMergeCounter = avlTreeArrayMergeCounter;
        treeList = new ArrayList<>();
        treeList.add(avlTree);
    }

    @Override
    public abstract IAvlTree substring(long leftInclusive, long rightExclusive);

    @Override
    public void append(IAvlTree avlTree) {
        treeList.add(avlTree);
    }

    @Override
    public void append(Iterable<IAvlTree> avlTrees) {
        for (IAvlTree tree : avlTrees)
            treeList.add(tree);
    }

    @Override
    public IAvlTree getTree() {
        mergeAll();
        return treeList.get(0);
    }

    @Override
    public IAvlTree[] getTrees() {
        return treeList.toArray(new IAvlTree[0]);
    }

    protected void mergeAll() {
        IAvlTree[] array = treeList.toArray(new IAvlTree[0]);
        nodesCacheStatisticsCounter.flushEvent(array);
        IAvlTree mergedTree = avlTreeArrayMerger.merge(array, rebalancingCounter, avlTreeArrayMergeCounter);

        treeList = new ArrayList<>();
        treeList.add(mergedTree);

        mergedTree.disposeAllButThis();
    }
}
