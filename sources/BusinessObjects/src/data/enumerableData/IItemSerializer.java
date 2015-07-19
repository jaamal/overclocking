package data.enumerableData;

import java.nio.ByteBuffer;

public interface IItemSerializer<T>
{
    int itemSizeInBytes();
    void serialize(T obj, ByteBuffer buffer);
    T deserialize(ByteBuffer buffer);
}
