package avlTree;

import tree.nodeProviders.*;
import tree.nodeProviders.indexSets.FreeNodesSet;
import avlTree.nodes.AvlTreeNode;
import avlTree.nodes.AvlTreeNodeBuilder;
import avlTree.nodes.AvlTreeNodeSerializer;
import caching.connections.TemporaryFileFactory;
import commons.settings.ISettings;
import data.enumerableData.IEnumerableData;
import data.enumerableData.IItemSerializer;
import data.enumerableData.InMemoryEnumerableData;
import data.enumerableData.MemoryMappedFileEnumerableData;
import data.longArray.LongSerializer;
import dataContracts.DataFactoryType;

public class AvlTreeManagerFactory implements IAvlTreeManagerFactory {
    private DataFactoryType dataFactoryType;
    private ISettings settings;

    public AvlTreeManagerFactory(ISettings settings, DataFactoryType dataFactoryType) {
        this.settings = settings;
        this.dataFactoryType = dataFactoryType;
    }

    public IAvlTreeManager create() {
        IEnumerableData<AvlTreeNode> nodeStorage;
        IEnumerableData<Long> innerReferencesStorage;
        switch (dataFactoryType) {
            case memory:
                nodeStorage = new InMemoryEnumerableData<>(AvlTreeNode.class);
                innerReferencesStorage = new InMemoryEnumerableData<>(Long.class);
                break;
            case file:
                IItemSerializer<AvlTreeNode> nodeSerializer = new AvlTreeNodeSerializer();
                TemporaryFileFactory temporaryFileFactory = new TemporaryFileFactory(settings);
                nodeStorage = new MemoryMappedFileEnumerableData<>(nodeSerializer, temporaryFileFactory, settings);
                innerReferencesStorage = new MemoryMappedFileEnumerableData<>(new LongSerializer(), temporaryFileFactory, settings);
                break;
            default:
                throw new RuntimeException(String.format("Unsupported DataFactoryType '%s'", dataFactoryType));
        }
        INodeAllocator<AvlTreeNode> nodeAllocator = new NodeAllocator<>(nodeStorage, innerReferencesStorage, new FreeNodesSet());
        ITreeNodeProvider<AvlTreeNode> nodeProvider = new TreeNodeProvider<>(nodeStorage, new AvlTreeNodeBuilder(nodeStorage), nodeAllocator);
        return new AvlTreeManager(nodeProvider);
    }
}
