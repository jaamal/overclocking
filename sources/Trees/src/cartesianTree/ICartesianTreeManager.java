package cartesianTree;

import cartesianTree.nodes.CartesianTreeNode;

public interface ICartesianTreeManager
{
    CartesianTree getEmptyTree();
    CartesianTree createNewTree(long value);
    CartesianTree createNewTree(CartesianTreeNode root);
    CartesianTree concatenate(CartesianTree leftTree, CartesianTree rightTree);
}
