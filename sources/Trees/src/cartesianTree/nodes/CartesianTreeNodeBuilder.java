package cartesianTree.nodes;

import tree.nodeProviders.ITreeNodeBuilder;
import data.enumerableData.IEnumerableData;

public class CartesianTreeNodeBuilder implements ITreeNodeBuilder<CartesianTreeNode>
{
    private IEnumerableData<CartesianTreeNode> storage;

    public CartesianTreeNodeBuilder(IEnumerableData<CartesianTreeNode> storage)
    {
        this.storage = storage;
    }

    @Override
    public CartesianTreeNode buildNewNode(long number, long left, long right, long value)
    {
        long count = getCount(left, right);
        return new CartesianTreeNode(number, left, right, value, count);
    }

    private long getCount(long left, long right)
    {
        long result = getCountByNumber(left) + getCountByNumber(right);
        if (result == 0)
            ++result;
        return result;
    }

    private long getCountByNumber(long number)
    {
        CartesianTreeNode node = storage.load(number);
		if (node == null)
			return 0;
		return node.count;
    }
}
