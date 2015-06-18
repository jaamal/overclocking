package caching.serializers;

public interface ISerializer<T>
{
    int size();
    
    byte[] serialize(T obj);
    void serialize(T obj, byte[] array, int offset);

    T deserialize(byte[] array);
    T deserialize(byte[] array, int offset);
}
