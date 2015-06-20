package tests.stress.Caching;

import caching.serializers.ISerializer;

import java.nio.ByteBuffer;

public class IntegerSerializer implements ISerializer<Integer>
    {
        @Override
        public int sizeInBytes()
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
            ByteBuffer buffer = ByteBuffer.wrap(array, offset, sizeInBytes());
            buffer.putInt(obj);
        }

        @Override
        public Integer deserialize(byte[] array, int offset)
        {
            ByteBuffer buffer = ByteBuffer.wrap(array, offset, sizeInBytes());
            return buffer.getInt();
        }
    }
