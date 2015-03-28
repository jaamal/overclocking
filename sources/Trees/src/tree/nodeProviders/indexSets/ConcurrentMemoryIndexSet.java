package tree.nodeProviders.indexSets;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentMemoryIndexSet implements IIndexSet {
    private final ArrayList<Long> stack;
    private final AtomicInteger count;

    public ConcurrentMemoryIndexSet() {
        count = new AtomicInteger(0);
        stack = new ArrayList<>();
    }

    @Override
    public void add(long index) {
        int current = count.getAndIncrement();
        while (stack.size() <= current)
            stack.add((long) -1);
        stack.set(current, index);
    }

    @Override
    public void close() {
    }

    @Override
    public long count() {
        return count.get();
    }

    @Override
    public long tryPopAny() {
        while (true) {
            int localCount = count.get();
            if (localCount == 0)
                return -1;
            long result = stack.get(localCount - 1);
            if (result == -1)
                return -1;
            if (count.compareAndSet(localCount, localCount - 1))
                return result;
        }
    }
}
