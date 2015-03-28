package avlTree.mergers;

import dataContracts.AvlMergePattern;

public interface IAvlTreeArrayMergerFactory {
    IAvlTreeArrayMerger create(AvlMergePattern mergePattern);
}
