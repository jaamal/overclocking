package avlTree;

import avlTree.nodes.AvlTreeNode;
import avlTree.nodes.AvlTreeNodeBuilder;
import caching.ConcurrentMemoryStorage;
import caching.IStorage;
import dataContracts.DataFactoryType;
import tree.nodeProviders.ConcurrentNodeAllocator;
import tree.nodeProviders.INodeAllocator;
import tree.nodeProviders.ITreeNodeProvider;
import tree.nodeProviders.TreeNodeProvider;
import tree.nodeProviders.indexSets.ConcurrentFreeNodesSet;

public class ConcurrentAvlTreeManagerFactory implements IAvlTreeManagerFactory {
    private DataFactoryType dataFactoryType;

    public ConcurrentAvlTreeManagerFactory(DataFactoryType dataFactoryType) {
        this.dataFactoryType = dataFactoryType;
    }

    public IAvlTreeManager create() {
        IStorage<AvlTreeNode> nodeStorage;
        switch (dataFactoryType) {
            case memory:
                nodeStorage = new ConcurrentMemoryStorage<>();
                break;
            default:
                throw new RuntimeException(String.format("Unsupported DataFactoryType '%s'", dataFactoryType));
        }
        AvlTreeNodeBuilder avlTreeNodeFactory = new AvlTreeNodeBuilder(nodeStorage);
        INodeAllocator<AvlTreeNode> nodeAllocator = new ConcurrentNodeAllocator<>(new ConcurrentFreeNodesSet());
        ITreeNodeProvider<AvlTreeNode> nodeProvider = new TreeNodeProvider<>(nodeStorage, avlTreeNodeFactory, nodeAllocator);
        return new AvlTreeManager(nodeProvider);
    }
}
