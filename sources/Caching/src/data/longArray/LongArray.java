package data.longArray;

import data.enumerableData.IEnumerableData;

public class LongArray implements ILongArray
{
    private IEnumerableData<Long> data;
    private long length;

    public LongArray(IEnumerableData<Long> data, long length)
    {
        this.data = data;
        this.length = length;
    }

    @Override
    public long get(long index)
    {
        return data.load(index);
    }

    @Override
    public void set(long index, long value)
    {
        data.save(index, value);
    }

    @Override
    public long size()
    {
        return length;
    }

    @Override
    public void close()
    {
        data.close();
    }
}
