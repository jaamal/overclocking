package compressingCore.dataAccess;

public class MemoryLongArray implements ILongArray
{
    private long[] array;

    public MemoryLongArray(int size)
    {
        array = new long[size];
    }

    public MemoryLongArray(long[] array)
    {
        this.array = array;
    }

    @Override
    public long get(long index)
    {
        return array[(int)index];
    }

    @Override
    public void set(long index, long value)
    {
        array[(int)index] = value;
    }

    @Override
    public long size()
    {
        return array.length;
    }

    @Override
    public void close()
    {
        array = null;
    }
}
