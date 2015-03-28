package avlTree.nodes;

import tree.ITreeNode;

public class AvlTreeNode implements ITreeNode
{
    public final long number;
    public final long leftSonNumber;
    public final long rightSonNumber;
    public final long value;
    public final long height;
    public final long width;
    public final long cutPosition;
    public final boolean isBalanced;

    public AvlTreeNode(
            long number,
            long leftSonNumber,
            long rightSonNumber,
            long value,
            long height,
            long width,
            long cutPosition,
            boolean isBalanced)
    {
        this.number = number;
        this.leftSonNumber = leftSonNumber;
        this.rightSonNumber = rightSonNumber;
        this.value = value;
        this.height = height;
        this.width = width;
        this.cutPosition = cutPosition;
        this.isBalanced = isBalanced;
    }

    public long getCutPosition()
    {
        return cutPosition;
    }

    public long getHeight()
    {
        return height;
    }

    public boolean isLeaf()
    {
        return leftSonNumber == -1 && rightSonNumber == -1;
    }

    @Override
    public long getLeftSonNumber()
    {
        return leftSonNumber;
    }

    @Override
    public long getNumber()
    {
        return number;
    }

    @Override
    public long getRightSonNumber()
    {
        return rightSonNumber;
    }

    @Override
    public long getValue()
    {
        return value;
    }

    public long getWidth()
    {
        return width;
    }

    public boolean isBalanced()
    {
        return isBalanced;
    }
}

