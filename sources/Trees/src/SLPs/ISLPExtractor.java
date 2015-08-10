package SLPs;

import productEnumerator.IProductEnumerator;
import tree.ITree;
import tree.ITreeNode;

public interface ISLPExtractor
{
    <TNode extends ITreeNode> IProductEnumerator getSLP(ITree<TNode> tree);
}
