package tree.nodeProviders;

import tree.ITreeNode;

public interface ITreeNodeBuilder<TNode extends ITreeNode>
{
    TNode buildNewNode(long number, long left, long right, long value);
}
