package data.charArray;

import java.nio.ByteBuffer;

import data.enumerableData.IItemSerializer;

public class CharSerializer implements IItemSerializer<Character>
{
    @Override
    public int itemSizeInBytes()
    {
        return 1;
    }

    @Override
    public Character deserialize(ByteBuffer buffer) {
        return (char) buffer.get();
    }

    @Override
    public void serialize(Character obj, ByteBuffer buffer) {
        buffer.put((byte)obj.charValue());
    }
}
