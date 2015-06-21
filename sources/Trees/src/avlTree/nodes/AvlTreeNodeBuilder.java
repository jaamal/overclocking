package avlTree.nodes;

import tree.nodeProviders.ITreeNodeBuilder;
import caching.IEnumerableData;

public class AvlTreeNodeBuilder implements ITreeNodeBuilder<AvlTreeNode> {
    private IEnumerableData<AvlTreeNode> storage;

    public AvlTreeNodeBuilder(IEnumerableData<AvlTreeNode> storage) {
        this.storage = storage;
    }

    @Override
    public AvlTreeNode buildNewNode(long number, long left, long right, long value) {
        if (left == -1 && right == -1)
            return new AvlTreeNode(number, -1, -1, value, 1, 1, 0, true);

        AvlTreeNode leftSon = storage.load(left);
        AvlTreeNode rightSon = storage.load(right);

        long height = Math.max(leftSon.height, rightSon.height) + 1;
        long width = leftSon.width + rightSon.width;
        long cutPosition = leftSon.width;
        boolean balanced = leftSon.isBalanced && rightSon.isBalanced &&
                Math.abs(leftSon.height - rightSon.height) <= 1;

        return new AvlTreeNode(number, left, right, value, height, width, cutPosition, balanced);
    }
}
