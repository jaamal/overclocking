package data.enumerableData;

import org.apache.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ConcurrentInMemoryEnumerableData<T> implements IEnumerableData<T> {
    private static Logger logger = Logger.getLogger(ConcurrentInMemoryEnumerableData.class);

    private final ArrayList<T[]> batches;
    private final int batchSize;
    private final Class<T> itemClass;
    private volatile long size;
    private final Object lockObject = new Object();

    public ConcurrentInMemoryEnumerableData(Class<T> itemClass) {
        this(itemClass, 100000);
    }

    public ConcurrentInMemoryEnumerableData(Class<T> itemClass, int batchSize) {
        this.itemClass = itemClass;
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
        return safeGetBatch((int) (index / batchSize))[(int) (index % batchSize)];

    }

    @SuppressWarnings("unchecked")
    @Override
    public void save(long index, T object) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        if (index >= size) {
            synchronized (lockObject) {
                while (index >= size) {
                    batches.add((T[])Array.newInstance(itemClass, batchSize));
                    size += batchSize;
                }
            }
        }
        safeGetBatch((int) (index / batchSize))[(int) (index % batchSize)] = object;
    }

    @Override
    public void close() {
    }

    private T[] safeGetBatch(int batchNumber) {
        for (int i = 0; i < 10; ++i) {
            try {
                return batches.get(batchNumber);
            } catch (IndexOutOfBoundsException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Throws exception while getting element of ArrayList on attempt " + i, e);
            }
        }
        throw new RuntimeException("Can not get element of ArrayList in 10 attempts");
    }
}
