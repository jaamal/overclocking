package avlTree;

import avlTree.nodes.AvlTreeNode;

public interface IAvlTreeManager
{
    IAvlTree concatenate(IAvlTree left, IAvlTree right);
    IAvlTree createNewTree(AvlTreeNode root);
    IAvlTree createNewTree(long value);
    IAvlTree createEmptyTree();

    void dispose();

    void disposeAllBut(long[] numbers);
}
