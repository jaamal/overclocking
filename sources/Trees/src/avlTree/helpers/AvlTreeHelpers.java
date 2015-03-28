package avlTree.helpers;

import avlTree.IAvlTree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class AvlTreeHelpers {
    public static Stack<IAvlTree> getRightmostPathToNodeOfFixedHeight(IAvlTree tree, long height) {
        if (tree.getHeight() < height)
            throw new IllegalStateException("The height of node is smaller than " + height);

        Stack<IAvlTree> result = new Stack<IAvlTree>();
        IAvlTree current = tree;
        while (current.getHeight() > height + 1) {
            result.push(current);
            current = (IAvlTree) current.getRightSubTree();
        }
        result.push(current);
        return result;
    }

    public static Stack<IAvlTree> getLeftmostPathToNodeOfFixedHeight(IAvlTree tree, long height) {
        if (tree.getHeight() < height)
            throw new IllegalStateException("The height of node is smaller than " + height);

        Stack<IAvlTree> result = new Stack<IAvlTree>();
        IAvlTree current = tree;
        while (current.getHeight() > height + 1) {
            result.push(current);
            current = (IAvlTree) current.getLeftSubTree();
        }
        result.push(current);
        return result;
    }

    public static IAvlTree substring(IAvlTree tree, long leftPosition, long rightPosition, IRebalancingCounter rebalancingCounter) {
        if (tree.isEmpty())
            throw new IllegalStateException("The tree is empty!");
        if (leftPosition < 0 || rightPosition > tree.getWidth() || leftPosition >= rightPosition)
            throw new IllegalStateException("leftPosition = " + leftPosition + ",rightPosition = " + rightPosition);

        return substringIteration(tree, leftPosition, rightPosition, rebalancingCounter);
    }

    private static IAvlTree substringIteration(IAvlTree tree, long l, long r, IRebalancingCounter rebalancingCounter) {
        if (l == 0 && r == tree.getWidth())
            return tree;

        long cutPosition = tree.getRoot().cutPosition;
        if (r <= cutPosition) {
            IAvlTree leftNode = (IAvlTree) tree.getLeftSubTree();
            return substringIteration(leftNode, l, Math.min(r, cutPosition), rebalancingCounter);
        } else if (l >= cutPosition) {
            IAvlTree rightNode = (IAvlTree) tree.getRightSubTree();
            return substringIteration(rightNode, Math.max(0, l - cutPosition), r - cutPosition, rebalancingCounter);
        } else {
            IAvlTree leftNode = (IAvlTree) tree.getLeftSubTree();
            IAvlTree leftSubTree = substringIteration(leftNode, l, Math.min(r, cutPosition), rebalancingCounter);
            IAvlTree rightNode = (IAvlTree) tree.getRightSubTree();
            IAvlTree rightSubTree = substringIteration(rightNode, Math.max(0, l - cutPosition), r - cutPosition, rebalancingCounter);

            return leftSubTree.merge(rightSubTree, rebalancingCounter);
        }
    }
}
