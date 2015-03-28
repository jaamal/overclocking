package avlTree;

import compressingCore.dataAccess.LongSerializer;
import tree.nodeProviders.*;
import tree.nodeProviders.indexSets.FreeNodesSet;
import avlTree.nodes.AvlTreeNode;
import avlTree.nodes.AvlTreeNodeBuilder;
import avlTree.nodes.AvlTreeNodeSerializer;
import caching.IStorage;
import caching.MemoryMappedFileStorage;
import caching.MemoryStorage;
import caching.connections.TemporaryFileFactory;
import caching.serializers.ISerializer;

import commons.settings.ISettings;

import dataContracts.DataFactoryType;

public class AvlTreeManagerFactory implements IAvlTreeManagerFactory {
    private DataFactoryType dataFactoryType;
    private ISettings settings;

    public AvlTreeManagerFactory(ISettings settings, DataFactoryType dataFactoryType) {
        this.settings = settings;
        this.dataFactoryType = dataFactoryType;
    }

    public IAvlTreeManager create() {
        IStorage<AvlTreeNode> nodeStorage;
        IStorage<Long> innerReferencesStorage;
        switch (dataFactoryType) {
            case memory:
                nodeStorage = new MemoryStorage<>();
                innerReferencesStorage = new MemoryStorage<>();
                break;
            case file:
                ISerializer<AvlTreeNode> nodeSerializer = new AvlTreeNodeSerializer();
                TemporaryFileFactory temporaryFileFactory = new TemporaryFileFactory(settings);
                nodeStorage = new MemoryMappedFileStorage<>(nodeSerializer, temporaryFileFactory, settings);
                innerReferencesStorage = new MemoryMappedFileStorage<>(new LongSerializer(), temporaryFileFactory, settings);
                break;
            default:
                throw new RuntimeException(String.format("Unsupported DataFactoryType '%s'", dataFactoryType));
        }
        INodeAllocator<AvlTreeNode> nodeAllocator = new NodeAllocator<>(nodeStorage, innerReferencesStorage, new FreeNodesSet());
        ITreeNodeProvider<AvlTreeNode> nodeProvider = new TreeNodeProvider<>(nodeStorage, new AvlTreeNodeBuilder(nodeStorage), nodeAllocator);
        return new AvlTreeManager(nodeProvider);
    }
}
