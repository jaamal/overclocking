package avlTree.treeSets;

import avlTree.IAvlTree;

public interface IAvlTreeSet {
    void addSymbol(long offset, long symbol);

    void addSubstring(long offset, long beginPosition, long length);

    void mergeNeighboringTree();

    IAvlTree getSingleTree();
}
