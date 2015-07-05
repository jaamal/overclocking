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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (cutPosition ^ (cutPosition >>> 32));
        result = prime * result + (int) (height ^ (height >>> 32));
        result = prime * result + (isBalanced ? 1231 : 1237);
        result = prime * result + (int) (leftSonNumber ^ (leftSonNumber >>> 32));
        result = prime * result + (int) (number ^ (number >>> 32));
        result = prime * result + (int) (rightSonNumber ^ (rightSonNumber >>> 32));
        result = prime * result + (int) (value ^ (value >>> 32));
        result = prime * result + (int) (width ^ (width >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AvlTreeNode other = (AvlTreeNode) obj;
        if (cutPosition != other.cutPosition)
            return false;
        if (height != other.height)
            return false;
        if (isBalanced != other.isBalanced)
            return false;
        if (leftSonNumber != other.leftSonNumber)
            return false;
        if (number != other.number)
            return false;
        if (rightSonNumber != other.rightSonNumber)
            return false;
        if (value != other.value)
            return false;
        if (width != other.width)
            return false;
        return true;
    }
}

