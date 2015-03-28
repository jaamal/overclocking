package avlTree;

import avlTree.nodes.AvlTreeNode;
import tree.nodeProviders.ITreeNodeProvider;

public class AvlTreeManager implements IAvlTreeManager
{
    private ITreeNodeProvider<AvlTreeNode> nodeProvider;

    public AvlTreeManager(ITreeNodeProvider<AvlTreeNode> nodeProvider)
    {
        this.nodeProvider = nodeProvider;
    }

    @Override
    public IAvlTree concatenate(IAvlTree left, IAvlTree right)
    {
        if (left.isEmpty())
            return right;
        if (right.isEmpty())
            return left;
        return createNewTree(nodeProvider.create(left.getRoot().number, right.getRoot().number, -1));
    }

    @Override
    public IAvlTree createNewTree(AvlTreeNode root)
    {
        return new AvlTree(root, nodeProvider, this);
    }

    @Override
    public IAvlTree createNewTree(long value)
    {
        return createNewTree(nodeProvider.create(-1, -1, value));
    }

    @Override
    public IAvlTree createEmptyTree()
    {
        return new AvlTree(null, nodeProvider, this);
    }

    @Override
    public void dispose(){
        nodeProvider.close();
    }

    @Override
    public void disposeAllBut(long[] numbers) {
        nodeProvider.disposeAllBut(numbers);
    }
}
