package tree.nodeProviders;

import tree.ITreeNode;
import tree.nodeProviders.indexSets.IFreeNodesSet;

public class ConcurrentNodeAllocator<TNode extends ITreeNode> implements INodeAllocator<TNode> {
    private final IFreeNodesSet freeNodesSet;

    public ConcurrentNodeAllocator(IFreeNodesSet freeNodesSet) {
        this.freeNodesSet = freeNodesSet;
    }

    @Override
    public void processNewCreation(long number, TNode node) {
    }

    @Override
    public void disposeAllBut(long number) {
        disposeAllBut(new long[]{number});
    }

    @Override
    public void disposeAllBut(long[] numbers) {

    }

    @Override
    public long getAnyFreeNodeNumber() {
        return freeNodesSet.getAnyFreeNodeNumber();
    }

    @Override
    public void close() {
        freeNodesSet.close();
    }

    @Override
    public long getTotalCount() {
        return freeNodesSet.getTotalCount();
    }
}
