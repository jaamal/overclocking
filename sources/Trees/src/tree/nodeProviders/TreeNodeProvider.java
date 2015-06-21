package tree.nodeProviders;

import caching.IEnumerableData;
import tree.ITreeNode;

public class TreeNodeProvider<TNode extends ITreeNode> implements ITreeNodeProvider<TNode> {
    private final IEnumerableData<TNode> nodeStorage;
    private ITreeNodeBuilder<TNode> treeNodeBuilder;
    private INodeAllocator<TNode> nodeAllocator;

    public TreeNodeProvider(
            IEnumerableData<TNode> nodeStorage,
            ITreeNodeBuilder<TNode> treeNodeBuilder,
            INodeAllocator<TNode> nodeAllocator) {
        this.nodeStorage = nodeStorage;
        this.treeNodeBuilder = treeNodeBuilder;
        this.nodeAllocator = nodeAllocator;
    }

    @Override
    public TNode create(long left, long right, long value) {
        long number = nodeAllocator.getAnyFreeNodeNumber();
        TNode node = treeNodeBuilder.buildNewNode(number, left, right, value);
        nodeAllocator.processNewCreation(number, node);
        nodeStorage.save(number, node);
        return node;
    }

    @Override
    public TNode get(long number) {
        return nodeStorage.load(number);
    }

    @Override
    public long getCount() {
        return nodeAllocator.getTotalCount();
    }

    @Override
    public void close() {
        nodeStorage.close();
        nodeAllocator.close();
    }

    @Override
    public void disposeAllBut(long number) {
        nodeAllocator.disposeAllBut(number);
    }

    @Override
    public void disposeAllBut(long[] numbers) {
        nodeAllocator.disposeAllBut(numbers);
    }
}
