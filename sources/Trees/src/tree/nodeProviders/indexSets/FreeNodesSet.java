package tree.nodeProviders.indexSets;

public class FreeNodesSet implements IFreeNodesSet {

    private final IIndexSet freeNodes;
    private long count;

    public FreeNodesSet() {
        count = 0;
        freeNodes = new MemoryIndexSet();
    }

    @Override
    public void addFreeNode(long index) {
        freeNodes.add(index);
    }

    @Override
    public long getAnyFreeNodeNumber() {
        long result = freeNodes.tryPopAny();
        if (result != -1)
            return result;
        return count++;
    }

    @Override
    public long getTotalCount() {
        return count;
    }

    @Override
    public void close() {
    }
}

