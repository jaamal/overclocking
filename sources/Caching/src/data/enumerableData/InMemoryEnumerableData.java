package data.enumerableData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InMemoryEnumerableData<T> implements IEnumerableData<T> {
    private final ArrayList<T[]> batches;
    private final Class<T> itemClass;
    private final int batchSize;
    private long size;

    public InMemoryEnumerableData(Class<T> itemClass) {
      this(itemClass, 100000);
    }

    public InMemoryEnumerableData(Class<T> itemClass, int batchSize) {
        this.batchSize = batchSize;
        this.itemClass = itemClass;
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
        return batches.get((int) (index / batchSize))[(int) (index % batchSize)];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save(long index, T object) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        while (index >= size) {
            batches.add((T[])Array.newInstance(itemClass, batchSize));
            size += batchSize;
        }
        batches.get((int) (index / batchSize))[(int) (index % batchSize)] = object;
    }

    @Override
    public void close() {
    }
}

