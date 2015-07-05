package cartesianTree.nodes;

import java.nio.ByteBuffer;

import data.enumerableData.IItemSerializer;

public class CartesianTreeNodeSerializer implements IItemSerializer<CartesianTreeNode>
{
    private final static int size = 40;

    @Override
    public int itemSizeInBytes()
    {
        return size;
    }

    @Override
    public void serialize(CartesianTreeNode node, ByteBuffer buffer)
    {
        buffer.putLong(node.number);
        buffer.putLong(node.left);
        buffer.putLong(node.right);
        buffer.putLong(node.value);
        buffer.putLong(node.count);
    }

    @Override
    public CartesianTreeNode deserialize(ByteBuffer buffer) {
        long number = buffer.getLong();
        long left = buffer.getLong();
        long right = buffer.getLong();
        long value = buffer.getLong();
        long count = buffer.getLong();
        return new CartesianTreeNode(number, left, right, value, count);
    }
}
