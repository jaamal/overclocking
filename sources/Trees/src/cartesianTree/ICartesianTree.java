package cartesianTree;

import cartesianTree.nodes.CartesianTreeNode;
import tree.ITree;

public interface ICartesianTree extends ITree<CartesianTreeNode>
{
	ICartesianTree merge(ICartesianTree other);
	
	ICartesianTree[] split(long pos);

	ICartesianTree substring(long left, long right);
	
	boolean isEmpty();
	
	long length();

    void dispose();
}
