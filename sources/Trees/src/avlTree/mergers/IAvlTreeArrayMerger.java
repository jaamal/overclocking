package avlTree.mergers;

import avlTree.IAvlTree;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.IRebalancingCounter;

public interface IAvlTreeArrayMerger {
    IAvlTree merge(IAvlTree[] trees, IRebalancingCounter rebalancingCounter, IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter);
}
