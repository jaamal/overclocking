package tests.stress.BusinessObjects;

import java.nio.ByteBuffer;

import data.enumerableData.IItemSerializer;

public class IntegerSerializer implements IItemSerializer<Integer>
{
    @Override
    public int itemSizeInBytes()
    {
        return 4;
    }

    @Override
    public Integer deserialize(ByteBuffer buffer) {
        return buffer.getInt();
    }

    @Override
    public void serialize(Integer obj, ByteBuffer buffer) {
        buffer.putInt(obj);
    }
}
