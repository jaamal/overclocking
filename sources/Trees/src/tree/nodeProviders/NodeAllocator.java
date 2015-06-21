package tree.nodeProviders;

import tree.ITreeNode;
import tree.nodeProviders.indexSets.IFreeNodesSet;

import java.util.HashSet;

import data.enumerableData.IEnumerableData;

public class NodeAllocator<TNode extends ITreeNode> implements INodeAllocator<TNode> {
    private final IEnumerableData<TNode> nodeStorage;
    private final IEnumerableData<Long> innerReferenceStorage;
    private final IFreeNodesSet freeNodesSet;
    private final HashSet<Long> withoutInner;

    public NodeAllocator(
            IEnumerableData<TNode> nodeStorage,
            IEnumerableData<Long> innerReferenceStorage,
            IFreeNodesSet freeNodesSet) {
        this.nodeStorage = nodeStorage;
        this.innerReferenceStorage = innerReferenceStorage;
        this.freeNodesSet = freeNodesSet;
        withoutInner = new HashSet<>();
    }

    @Override
    public void processNewCreation(long number, TNode node) {
        changeInnerReferences(node.getLeftSonNumber(), 1);
        changeInnerReferences(node.getRightSonNumber(), 1);
        if (innerReferenceStorage.load(number) == 0)
            withoutInner.add(number);
    }

    @Override
    public void disposeAllBut(long number) {
        disposeAllBut(new long[]{number});
    }

    @Override
    public void disposeAllBut(long[] numbers) {
        for (long number : numbers)
            changeInnerReferences(number, 1);
        while (!withoutInner.isEmpty()) {
            Long[] array = withoutInner.toArray(new Long[0]);
            withoutInner.clear();
            for (Long index : array)
                dispose(index);
        }
        for (long number : numbers)
            changeInnerReferences(number, -1);
    }

    @Override
    public long getAnyFreeNodeNumber() {
        long result = freeNodesSet.getAnyFreeNodeNumber();
        innerReferenceStorage.save(result, (long) 0);
        return result;
    }

    @Override
    public void close() {
        freeNodesSet.close();
    }

    @Override
    public long getTotalCount() {
        return freeNodesSet.getTotalCount();
    }

    private void changeInnerReferences(long number, long delta) {
        if (number < 0)
            return;
        long innerReferenceCount = innerReferenceStorage.load(number);
        if (innerReferenceCount == 0)
            withoutInner.remove(number);
        if (innerReferenceCount + delta == 0)
            withoutInner.add(number);
        innerReferenceStorage.save(number, innerReferenceCount + delta);
    }

    private void dispose(long index) {
        TNode node = nodeStorage.load(index);
        freeNodesSet.addFreeNode(index);
        changeInnerReferences(node.getLeftSonNumber(), -1);
        changeInnerReferences(node.getRightSonNumber(), -1);
    }
}

