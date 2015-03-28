package compressingCore.dataAccess;

import caching.serializers.ISerializer;

public class CharSerializer implements ISerializer<Character>
{
    @Override
    public int size()
    {
        return 1;
    }

    @Override
    public byte[] serialize(Character obj)
    {
        byte[] result = new byte[1];
        serialize(obj, result, 0);
        return result;
    }

    @Override
    public Character deserialize(byte[] array)
    {
        return deserialize(array, 0);
    }

    @Override
    public void serialize(Character obj, byte[] array, int offset)
    {
        array[offset] = (byte) obj.charValue();
    }

    @Override
    public Character deserialize(byte[] array, int offset)
    {
        return (char) array[offset];
    }
}
