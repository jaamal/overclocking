package data.enumerableData;

public interface IItemSerializer<T>
{
    int itemSizeInBytes();
    
    byte[] serialize(T obj);
    void serialize(T obj, byte[] array, int offset);

    T deserialize(byte[] array);
    T deserialize(byte[] array, int offset);
}
