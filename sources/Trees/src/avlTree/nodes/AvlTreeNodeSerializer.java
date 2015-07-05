package avlTree.nodes;

import java.nio.ByteBuffer;

import data.enumerableData.IItemSerializer;

public class AvlTreeNodeSerializer implements IItemSerializer<AvlTreeNode>
{
    private final static int size = 57;

    @Override
    public int itemSizeInBytes()
    {
        return size;
    }

    @Override
    public void serialize(AvlTreeNode obj, ByteBuffer buffer) {
        buffer.putLong(obj.number);
        buffer.putLong(obj.leftSonNumber);
        buffer.putLong(obj.rightSonNumber);
        buffer.putLong(obj.value);
        buffer.putLong(obj.height);
        buffer.putLong(obj.width);
        buffer.putLong(obj.cutPosition);
        buffer.put(obj.isBalanced ? (byte) 1 : (byte) 0);
    }

    @Override
    public AvlTreeNode deserialize(ByteBuffer buffer) {
        long number = buffer.getLong();
        long leftSonNumber = buffer.getLong();
        long rightSonNumber = buffer.getLong();
        long value = buffer.getLong();
        long height = buffer.getLong();
        long width = buffer.getLong();
        long cutPosition = buffer.getLong();
        boolean isBalanced = buffer.get() != 0;
        return new AvlTreeNode(number, leftSonNumber, rightSonNumber, value, height, width, cutPosition, isBalanced);
    }
}
