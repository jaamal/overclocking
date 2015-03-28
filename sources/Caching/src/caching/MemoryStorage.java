package caching;

import java.util.ArrayList;

public class MemoryStorage<T> implements IStorage<T> {
    private final int batchSize;
    private final ArrayList<Object[]> batches;
    private long size;

    public MemoryStorage()
    {
      this(100000);
    }

    public MemoryStorage(int batchSize) {
        this.batchSize = batchSize;
        batches = new ArrayList<>();
        size = 0;
    }

    @Override
    public T load(long index) {
        if (index == -1)
            return null;
        if (index < 0)
            throw new IndexOutOfBoundsException();
        if (index >= size)
            throw new IndexOutOfBoundsException();
        return (T) batches.get((int) (index / batchSize))[(int) (index % batchSize)];
    }

    @Override
    public void save(long index, T object) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        while (index >= size) {
            batches.add(new Object[batchSize]);
            size += batchSize;
        }
        batches.get((int) (index / batchSize))[(int) (index % batchSize)] = object;
    }

    @Override
    public void close() {
    }
}

