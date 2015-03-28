package avlTree.treeSets;

import avlTree.IAvlTree;
import avlTree.slpBuilders.ConcurrentAvlBuilderStopwatches;

public interface IAvlTreeSet {
    void addSymbol(long offset, long symbol);

    void addSubstring(long offset, long beginPosition, long length);

    void mergeNeighboringTree(ConcurrentAvlBuilderStopwatches stopwatches);

    IAvlTree getSingleTree();
}
