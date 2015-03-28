package caching;

public interface IByteArrayStorageFactory<T>
{
    ByteArrayStorage<T> create(byte[] data);
}
