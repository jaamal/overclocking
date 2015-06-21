package compressingCore.dataAccess;

import commons.utils.NumericUtils;
import data.enumerableData.IItemSerializer;

public class LongSerializer implements IItemSerializer<Long>
{
    @Override
    public int itemSizeInBytes()
    {
        return 8;
    }

    @Override
    public byte[] serialize(Long obj)
    {
        byte[] result = new byte[8];
        serialize(obj, result, 0);
        return result;
    }

    @Override
    public Long deserialize(byte[] array)
    {
        return deserialize(array, 0);
    }

    @Override
    public void serialize(Long obj, byte[] array, int offset)
    {
        NumericUtils.longToBytes(obj, array, 0);
    }

    @Override
    public Long deserialize(byte[] array, int offset)
    {
        return NumericUtils.bytesToLong(array, offset);
    }
}
