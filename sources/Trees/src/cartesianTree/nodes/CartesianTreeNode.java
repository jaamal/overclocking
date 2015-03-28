package cartesianTree.nodes;

import tree.ITreeNode;

public class CartesianTreeNode implements ITreeNode
{
	public final long number;
	public final long left, right;
	public final long value;
	public final long count;

	public CartesianTreeNode(long number, long left, long right, long value, long count) {
		this.number = number;
		this.left = left;
		this.right = right;
		this.value = value;
		this.count = count;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof CartesianTreeNode) {
			CartesianTreeNode node = (CartesianTreeNode) obj;
			return this.number == node.number && this.left == node.left && this.right == node.right && this.value == node.value
					&& this.count == node.count;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		Long[] fields = new Long[] { number, left, right, value, count };
		int result = 0;
		for (int i = 0; i < fields.length; ++i)
			result = result * 7 + fields[i].hashCode();
		return result;
	}

    @Override
    public long getLeftSonNumber()
    {
        return left;
    }

    @Override
    public long getRightSonNumber()
    {
        return right;
    }

    @Override
    public long getNumber()
    {
        return number;
    }

    @Override
    public long getValue()
    {
        return value;
    }
}
