package caching;

import caching.serializers.ISerializer;

public class ByteArrayStorageFactory<T> implements IByteArrayStorageFactory<T>
{
    private final ISerializer<T> serializer;

    public ByteArrayStorageFactory(ISerializer<T> serializer)
    {
        this.serializer = serializer;
    }

    @Override
    public ByteArrayStorage<T> create(byte[] data)
    {
        return new ByteArrayStorage<T>(serializer, data);
    }
}
