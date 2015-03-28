package cartesianTree;

import cartesianTree.heapKeyResolvers.IHeapKeyResolver;
import cartesianTree.nodes.CartesianTreeNode;
import tree.nodeProviders.ITreeNodeProvider;

public class CartesianTreeManager implements ICartesianTreeManager
{
    private ITreeNodeProvider<CartesianTreeNode> nodeProvider;
    private IHeapKeyResolver heapKeyResolver;

    public CartesianTreeManager(
            ITreeNodeProvider<CartesianTreeNode> nodeProvider,
            IHeapKeyResolver heapKeyResolver)
    {
        this.nodeProvider = nodeProvider;
        this.heapKeyResolver = heapKeyResolver;
    }

    @Override
    public CartesianTree getEmptyTree()
    {
        return createNewTree(null);
    }

    @Override
    public CartesianTree createNewTree(long value)
    {
        return createNewTree(nodeProvider.create(-1, -1, value));
    }

    @Override
    public CartesianTree createNewTree(CartesianTreeNode root)
    {
        return new CartesianTree(root, nodeProvider, heapKeyResolver, this);
    }

    @Override
    public CartesianTree concatenate(CartesianTree leftTree, CartesianTree rightTree)
    {
	    if (leftTree.isEmpty())
			return rightTree;
		else if (rightTree.isEmpty())
			return leftTree;
		else
            return createNewTree(nodeProvider.create(leftTree.getRoot().number, rightTree.getRoot().number, -1));
    }
}
