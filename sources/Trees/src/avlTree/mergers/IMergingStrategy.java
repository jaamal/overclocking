package avlTree.mergers;

import avlTree.IAvlTree;
import avlTree.helpers.IRebalancingCounter;

public interface IMergingStrategy
{
    public IAvlTree apply(IRebalancingCounter rebalancingCounter);
}
