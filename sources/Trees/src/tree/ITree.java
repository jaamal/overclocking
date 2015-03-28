package tree;

public interface ITree<TNode extends ITreeNode> {
    TNode getRoot();

    boolean isEmpty();

    ITree<TNode> getLeftSubTree();

    ITree<TNode> getRightSubTree();

    void disposeAllButThis();
}
