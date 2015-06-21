package tests.stress.Caching;

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
        public byte[] serialize(Integer obj)
        {
            byte[] array = new byte[4];
            serialize(obj, array, 0);
            return array;
        }

        @Override
        public Integer deserialize(byte[] array)
        {
            return deserialize(array, 0);
        }

        @Override
        public void serialize(Integer obj, byte[] array, int offset)
        {
            ByteBuffer buffer = ByteBuffer.wrap(array, offset, itemSizeInBytes());
            buffer.putInt(obj);
        }

        @Override
        public Integer deserialize(byte[] array, int offset)
        {
            ByteBuffer buffer = ByteBuffer.wrap(array, offset, itemSizeInBytes());
            return buffer.getInt();
        }
    }
