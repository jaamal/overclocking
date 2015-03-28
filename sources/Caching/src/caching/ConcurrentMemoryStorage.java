package caching;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class ConcurrentMemoryStorage<T> implements IStorage<T> {
    private static Logger logger = Logger.getLogger(ConcurrentMemoryStorage.class);

    private final int batchSize;
    private final ArrayList<Object[]> batches;
    private volatile long size;
    private final Object lockObject = new Object();

    public ConcurrentMemoryStorage() {
        this(100000);
    }

    public ConcurrentMemoryStorage(int batchSize) {
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
        return (T) threadSafeGetBatch((int) (index / batchSize))[(int) (index % batchSize)];

    }

    @Override
    public void save(long index, T object) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        if (index >= size) {
            synchronized (lockObject) {
                while (index >= size) {
                    batches.add(new Object[batchSize]);
                    size += batchSize;
                }
            }
        }

        threadSafeGetBatch((int) (index / batchSize))[(int) (index % batchSize)] = object;
    }

    @Override
    public void close() {
    }

    private Object[] threadSafeGetBatch(int batchNumber) {
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
