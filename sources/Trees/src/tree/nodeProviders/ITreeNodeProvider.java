package tree.nodeProviders;

import tree.ITreeNode;

public interface ITreeNodeProvider<TNode extends ITreeNode> {
    TNode create(long left, long right, long value);

    TNode get(long number);

    long getCount();

    void close();

    void disposeAllBut(long number);

    void disposeAllBut(long[] numbers);
}