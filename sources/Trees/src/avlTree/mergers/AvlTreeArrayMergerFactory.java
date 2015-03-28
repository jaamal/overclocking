package avlTree.mergers;

import dataContracts.AvlMergePattern;

public class AvlTreeArrayMergerFactory implements IAvlTreeArrayMergerFactory {

    @Override
    public IAvlTreeArrayMerger create(AvlMergePattern mergePattern) {
        switch (mergePattern) {
            case sequential:
                return new AvlTreeArraySequentialMerger();
            case block:
                return new AvlTreeArrayBlockMerger(new AvlTreeArraySequentialMerger());
            case block2:
                return new AvlTreeArrayBlock2Merger(new AvlTreeArraySequentialMerger());
            case recursiveBlock:
                return new AvlTreeArrayRecursiveBlockMerger();
            default:
                throw new RuntimeException("Unknown AvlTreeArrayMerger Type");
        }
    }

}
