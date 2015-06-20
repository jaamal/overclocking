package caching;

import caching.serializers.ISerializer;

public class ByteArrayStorage<T> implements IStorage<T>
{
    private final ISerializer<T> serializer;
    private final byte[] data;

    public ByteArrayStorage(ISerializer<T> serializer, byte[] data)
    {
        this.serializer = serializer;
        this.data = data.clone();
    }

    @Override
    public T load(long number)
    {
        return serializer.deserialize(data, (int)(serializer.sizeInBytes() * number));
    }

    @Override
    public void save(long number, T obj)
    {
        serializer.serialize(obj, data, (int)(serializer.sizeInBytes() * number));
    }

    @Override
    public void close()
    {
    }

    public byte[] getData()
    {
        return data.clone();
    }
}
