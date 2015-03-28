package cartesianTree.heapKeyResolvers;

import cartesianTree.nodes.CartesianTreeNode;

public interface IHeapKeyResolver
{
	HeapKeyResolution resolve(CartesianTreeNode leftRoot, CartesianTreeNode rightRoot);
}
