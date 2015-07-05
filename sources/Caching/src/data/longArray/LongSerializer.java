package data.longArray;

import java.nio.ByteBuffer;

import data.enumerableData.IItemSerializer;

public class LongSerializer implements IItemSerializer<Long>
{
    @Override
    public int itemSizeInBytes()
    {
        return 8;
    }

    @Override
    public Long deserialize(ByteBuffer buffer) {
        return buffer.getLong();
    }

    @Override
    public void serialize(Long obj, ByteBuffer buffer) {
        buffer.putLong(obj);
    }
}
