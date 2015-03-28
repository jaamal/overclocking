package avlTree.buffers;

import avlTree.IAvlTree;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.INodesCacheStatisticsCounter;
import avlTree.helpers.IRebalancingCounter;

public interface IAvlTreeBufferFactory {
    IAvlTreeBuffer create(IRebalancingCounter rebalanceCounter, INodesCacheStatisticsCounter nodesCacheStatisticsCounter, IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter, IAvlTree avlTree);
}
