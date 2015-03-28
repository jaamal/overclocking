package avlTree;

import avlTree.helpers.IRebalancingCounter;
import avlTree.nodes.AvlTreeNode;
import tree.ITree;

public interface IAvlTree extends ITree<AvlTreeNode>
{
    public IAvlTree merge(IAvlTree right, IRebalancingCounter rebalancingCounter);
    public IAvlTree split(long inclusiveStart, long exclusiveEnd, IRebalancingCounter rebalancingCounter);
    public long getHeight();
    public long getWidth();
    public void dispose();
}
