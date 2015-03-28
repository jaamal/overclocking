package tree.nodeProviders;

import tree.ITreeNode;

public interface INodeAllocator<TNode extends ITreeNode> {
    void processNewCreation(long number, TNode node);

    void disposeAllBut(long number);

    void disposeAllBut(long[] numbers);

    long getAnyFreeNodeNumber();

    void close();

    long getTotalCount();
}
