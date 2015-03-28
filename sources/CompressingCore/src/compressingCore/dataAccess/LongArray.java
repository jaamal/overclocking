package compressingCore.dataAccess;

import caching.IStorage;

public class LongArray implements ILongArray
{
    private IStorage<Long> storage;
    private long length;

    public LongArray(IStorage<Long> storage, long length)
    {
        this.storage = storage;
        this.length = length;
    }

    @Override
    public long get(long index)
    {
        return storage.load(index);
    }

    @Override
    public void set(long index, long value)
    {
        storage.save(index, value);
    }

    @Override
    public long size()
    {
        return length;
    }

    @Override
    public void close()
    {
        storage.close();
    }
}
