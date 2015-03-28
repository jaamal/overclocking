package avlTree.buffers;


import avlTree.IAvlTree;

public interface IAvlTreeBuffer {

    IAvlTree substring(long leftInclusive, long rightExclusive);

    void append(IAvlTree avlTree);

    void append(Iterable<IAvlTree> avlTrees);

    IAvlTree getTree();

    IAvlTree[] getTrees();
}
