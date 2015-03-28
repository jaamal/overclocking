package avlTree.buffers;

import avlTree.IAvlTree;
import avlTree.helpers.IAvlTreeArrayMergeCounter;
import avlTree.helpers.INodesCacheStatisticsCounter;
import avlTree.helpers.IRebalancingCounter;
import avlTree.mergers.IAvlTreeArrayMergerFactory;
import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;

public class AvlTreeBufferFactory implements IAvlTreeBufferFactory {
    private final IAvlTreeArrayMergerFactory avlTreeArrayMergerFactory;
    private final AvlSplitPattern splitPattern;
    private AvlMergePattern mergePattern;

    public AvlTreeBufferFactory(IAvlTreeArrayMergerFactory avlTreeArrayMergerFactory,
                                AvlMergePattern mergePattern,
                                AvlSplitPattern splitPattern) {
        this.avlTreeArrayMergerFactory = avlTreeArrayMergerFactory;
        this.mergePattern = mergePattern;
        this.splitPattern = splitPattern;
    }

    @Override
    public IAvlTreeBuffer create(IRebalancingCounter rebalanceCounter, INodesCacheStatisticsCounter nodesCacheStatisticsCounter, IAvlTreeArrayMergeCounter avlTreeArrayMergeCounter,  IAvlTree avlTree) {
        switch(splitPattern)
        {
            case fromMerged:
                return new AvlTreeSplitMergedBuffer(nodesCacheStatisticsCounter, avlTreeArrayMergerFactory.create(mergePattern), rebalanceCounter, avlTreeArrayMergeCounter, avlTree);
            case fromFirst:
                return new AvlTreeSplitFirstBuffer(nodesCacheStatisticsCounter, avlTreeArrayMergerFactory.create(mergePattern), rebalanceCounter, avlTreeArrayMergeCounter, avlTree);
            case fromAny:
                return new AvlTreeSplitAnyBuffer(nodesCacheStatisticsCounter, avlTreeArrayMergerFactory.create(mergePattern), rebalanceCounter, avlTreeArrayMergeCounter, avlTree);
            default:
                throw new RuntimeException("Unknown AvlTreeBuffer Type");
        }
    }
}
