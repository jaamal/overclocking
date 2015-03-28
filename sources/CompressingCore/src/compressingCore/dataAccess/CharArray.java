package compressingCore.dataAccess;

import caching.IStorage;

public class CharArray implements IReadableCharArray
{
    private final IStorage<Character> storage;
    private final long length;
    private final long start;

    public CharArray(IStorage<Character> storage, long length)
    {
        this(storage, length, 0);
    }

    private CharArray(IStorage<Character> storage, long length, long start)
    {
        this.storage = storage;
        this.length = length;
        this.start = start;
    }

    @Override
    public char get(long idx)
    {
        return storage.load(idx + start);
    }

    @Override
    public long length()
    {
        return length;
    }

    @Override
    public IReadableCharArray subArray(long inclusiveStartIndex, long exclusiveEndIndex)
    {
        return new CharArray(storage, exclusiveEndIndex - inclusiveStartIndex, inclusiveStartIndex);
    }

    @Override
    public void close()
    {
        storage.close();
    }
}
