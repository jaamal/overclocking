package SLPs;

import avlTree.slpBuilders.ISLPBuilder;
import tree.ITree;
import tree.ITreeNode;

public interface ISLPExtractor
{
    <TNode extends ITreeNode> ISLPBuilder getSLP(ITree<TNode> tree);
}
