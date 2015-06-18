package avlTree.nodes;

import java.nio.ByteBuffer;

import caching.serializers.ISerializer;

public class AvlTreeNodeSerializer implements ISerializer<AvlTreeNode>
{
    private final static int size = 57;

    @Override
    public byte[] serialize(AvlTreeNode node)
    {
        byte[] array = new byte[size];

        serialize(node, array, 0);

        return array;
    }

    @Override
    public AvlTreeNode deserialize(byte[] array)
    {
        if (array.length != size)
            throw new RuntimeException(String.format("Fail to deserialize node from bytes. Required bytes size: %d, but actual size: %d", size, array.length));
        return deserialize(array, 0);
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public AvlTreeNode deserialize(byte[] array, int offset)
    {
        if (offset + size > array.length)
            throw new RuntimeException(String.format("Not enough space to deserialize a node. Offset: %d, required size: %d, array size: %d", 
                    offset, size, array.length));

        ByteBuffer byteBuffer = ByteBuffer.wrap(array, offset, size);
        long number = byteBuffer.getLong();
        long leftSonNumber = byteBuffer.getLong();
        long rightSonNumber = byteBuffer.getLong();
        long value = byteBuffer.getLong();
        long height = byteBuffer.getLong();
        long width = byteBuffer.getLong();
        long cutPosition = byteBuffer.getLong();
        boolean isBalanced = byteBuffer.get() != 0;
        return new AvlTreeNode(number, leftSonNumber, rightSonNumber, value, height, width, cutPosition, isBalanced);
    }

    @Override
    public void serialize(AvlTreeNode node, byte[] array, int offset)
    {
        if (node == null)
            throw new NullPointerException("Fail to serialize null node.");

        if (offset + size > array.length)
            throw new RuntimeException(String.format("Not enough space to serialize a node. Offset: %d, required size: %d, array size: %d", 
                    offset, size, array.length));
        
        ByteBuffer byteBuffer = ByteBuffer.wrap(array, offset, size);
        byteBuffer.putLong(node.number);
        byteBuffer.putLong(node.leftSonNumber);
        byteBuffer.putLong(node.rightSonNumber);
        byteBuffer.putLong(node.value);
        byteBuffer.putLong(node.height);
        byteBuffer.putLong(node.width);
        byteBuffer.putLong(node.cutPosition);
        byteBuffer.put(node.isBalanced ? (byte) 1 : (byte) 0);
    }
}
