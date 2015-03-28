package tree.nodeProviders.indexSets;

import java.util.ArrayList;

public class MemoryIndexSet implements IIndexSet {
    private final ArrayList<Long> stack;

    public MemoryIndexSet() {
        stack = new ArrayList<>();
    }

    @Override
    public void add(long index) {
        stack.add(index);
    }

    @Override
    public void close() {
    }

    @Override
    public long count() {
        return stack.size();
    }

    @Override
    public long tryPopAny() {
        if (stack.size() == 0)
            return -1;
        long result = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        return result;
    }
}
