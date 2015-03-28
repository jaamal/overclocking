package tree.nodeProviders.indexSets;

import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentFreeNodesSet implements IFreeNodesSet {

    private AtomicLong count;
    private IIndexSet freeNodes;

    public ConcurrentFreeNodesSet() {
        count = new AtomicLong(0);
        freeNodes = new ConcurrentMemoryIndexSet();
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
        return count.getAndIncrement();
    }

    @Override
    public long getTotalCount() {
        return count.get();
    }

    @Override
    public void close() {
    }
}
