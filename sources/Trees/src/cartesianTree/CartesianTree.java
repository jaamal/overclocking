package cartesianTree;

import cartesianTree.exceptions.IncompatibleTreeTypesException;
import cartesianTree.heapKeyResolvers.HeapKeyResolution;
import cartesianTree.heapKeyResolvers.IHeapKeyResolver;
import cartesianTree.nodes.CartesianTreeNode;
import tree.nodeProviders.ITreeNodeProvider;

public class CartesianTree implements ICartesianTree {
    private final ITreeNodeProvider<CartesianTreeNode> nodeProvider;
    private final IHeapKeyResolver heapKeyResolver;
    private final ICartesianTreeManager treeManager;
    private final CartesianTreeNode root;

    public CartesianTree(
            CartesianTreeNode root,
            ITreeNodeProvider<CartesianTreeNode> nodeProvider,
            IHeapKeyResolver heapKeyResolver,
            ICartesianTreeManager treeManager) {
        this.root = root;
        this.nodeProvider = nodeProvider;
        this.heapKeyResolver = heapKeyResolver;
        this.treeManager = treeManager;
    }

    @Override
    public ICartesianTree merge(ICartesianTree other) {
        if (!(other instanceof CartesianTree))
            throw new IncompatibleTreeTypesException();

        return merge((CartesianTree) other);
    }

    private CartesianTree merge(CartesianTree other) {
        if (this.isEmpty())
            return other;
        if (other.isEmpty())
            return this;

        CartesianTreeNode newRoot = null;
        HeapKeyResolution resolution = heapKeyResolver.resolve(this.root, other.root);
        switch (resolution) {
            case Center: {
                newRoot = nodeProvider.create(this.root.number, other.root.number, -1);
                break;
            }
            case Left: {
                CartesianTree newNode = this.getRightSubTree().merge(other);
                newRoot = nodeProvider.create(this.root.left, newNode.root.number, -1);
                break;
            }
            case Right: {
                CartesianTree newNode = this.merge(other.getLeftSubTree());
                newRoot = nodeProvider.create(newNode.root.number, other.root.right, -1);
                break;
            }
        }
        return treeManager.createNewTree(newRoot);
    }

    @Override
    public ICartesianTree substring(long left, long right) {
        ICartesianTree[] array1 = this.split(right - 1);
        ICartesianTree[] array2 = array1[0].split(left - 1);
        return array2[1];
    }

    @Override
    public ICartesianTree[] split(long pos) {
        if (isEmpty()) // empty string
            return new ICartesianTree[]{treeManager.getEmptyTree(), treeManager.getEmptyTree()};
        if (pos < 0) // the cut at left of the first symbol
            return new ICartesianTree[]{treeManager.getEmptyTree(), this};
        if (pos >= this.root.count - 1) // the cut at right of the last symbol
            return new ICartesianTree[]{this, treeManager.getEmptyTree()};

        CartesianTree leftSubTree = getLeftSubTree();
        CartesianTree rightSubTree = getRightSubTree();
        long countLeft = leftSubTree.root.count;
        ICartesianTree[] array;
        if (pos < countLeft) // the cut at the left subtree
        {
            array = leftSubTree.split(pos);
            array[1] = treeManager.concatenate((CartesianTree) array[1], rightSubTree);
        } else // the cut at the right subtree
        {
            array = rightSubTree.split(pos - countLeft);
            array[0] = treeManager.concatenate(leftSubTree, (CartesianTree) array[0]);
        }

        return array;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public long length() {
        return (this.root == null) ? 0 : this.root.count;
    }

    public CartesianTree getRightSubTree() {
        return treeManager.createNewTree(nodeProvider.get(root.right));
    }

    @Override
    public void disposeAllButThis() {
        nodeProvider.disposeAllBut(root == null ? -1 : root.number);
    }

    public CartesianTree getLeftSubTree() {
        return treeManager.createNewTree(nodeProvider.get(root.left));
    }

    public CartesianTreeNode getRoot() {
        return root;
    }

    public void dispose() {
        nodeProvider.close();
    }
}
