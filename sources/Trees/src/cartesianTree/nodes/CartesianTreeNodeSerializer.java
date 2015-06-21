package cartesianTree.nodes;

import java.nio.ByteBuffer;

import data.enumerableData.IItemSerializer;

public class CartesianTreeNodeSerializer implements IItemSerializer<CartesianTreeNode>
{
    private final static int size = 40;

    @Override
    public byte[] serialize(CartesianTreeNode node)
    {
        byte[] array = new byte[size];

        serialize(node, array, 0);

        return array;
    }

    @Override
    public CartesianTreeNode deserialize(byte[] array)
    {
        if (array.length != size)
            throw new RuntimeException(String.format("Fail to deserialize node from bytes. Required bytes size: %d, but actual size: %d", size, array.length));
        return deserialize(array, 0);
    }

    @Override
    public int itemSizeInBytes()
    {
        return size;
    }

    @Override
    public CartesianTreeNode deserialize(byte[] array, int offset)
    {
        if (offset + size > array.length)
            throw new RuntimeException(String.format("Not enough space to deserialize a node. Offset: %d, required size: %d, array size: %d", 
                    offset, size, array.length));

        ByteBuffer byteBuffer = ByteBuffer.wrap(array, offset, size);
        long number = byteBuffer.getLong();
        long left = byteBuffer.getLong();
        long right = byteBuffer.getLong();
        long value = byteBuffer.getLong();
        long count = byteBuffer.getLong();
        return new CartesianTreeNode(number, left, right, value, count);
    }

    @Override
    public void serialize(CartesianTreeNode node, byte[] array, int offset)
    {
        if (node == null)
            throw new NullPointerException("Fail to serialize null node.");

        if (offset + size > array.length)
            throw new RuntimeException(String.format("Not enough space to serialize a node. Offset: %d, required size: %d, array size: %d", 
                    offset, size, array.length));

        ByteBuffer byteBuffer = ByteBuffer.wrap(array, offset, size);
        byteBuffer.putLong(node.number);
        byteBuffer.putLong(node.left);
        byteBuffer.putLong(node.right);
        byteBuffer.putLong(node.value);
        byteBuffer.putLong(node.count);
    }
}
