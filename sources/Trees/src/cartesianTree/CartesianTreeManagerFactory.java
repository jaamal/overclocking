package cartesianTree;

import caching.connections.TemporaryFileFactory;
import cartesianTree.heapKeyResolvers.IHeapKeyResolver;
import cartesianTree.heapKeyResolvers.RandomGeneratorFactory;
import cartesianTree.heapKeyResolvers.RandomHeapKeyResolver;
import cartesianTree.nodes.CartesianTreeNode;
import cartesianTree.nodes.CartesianTreeNodeBuilder;
import cartesianTree.nodes.CartesianTreeNodeSerializer;
import commons.settings.ISettings;
import compressingCore.dataAccess.LongSerializer;
import data.enumerableData.IEnumerableData;
import data.enumerableData.InMemoryEnumerableData;
import data.enumerableData.MemoryMappedFileEnumerableData;
import dataContracts.DataFactoryType;
import tree.nodeProviders.*;
import tree.nodeProviders.indexSets.FreeNodesSet;

public class CartesianTreeManagerFactory implements ICartesianTreeManagerFactory {
    private DataFactoryType dataFactoryType;
    private ISettings settings;

    public CartesianTreeManagerFactory(ISettings settings, DataFactoryType dataFactoryType) {
        this.settings = settings;
        this.dataFactoryType = dataFactoryType;
    }

    @Override
    public ICartesianTreeManager create() {
        IEnumerableData<CartesianTreeNode> nodeIStorage;
        IEnumerableData<Long> innerReferencesStorage;
        if (dataFactoryType == DataFactoryType.memory) {
            nodeIStorage = new InMemoryEnumerableData<>(CartesianTreeNode.class);
            innerReferencesStorage = new InMemoryEnumerableData<>(Long.class);
        } else if (dataFactoryType == DataFactoryType.file) {
            CartesianTreeNodeSerializer serializer = new CartesianTreeNodeSerializer();
            TemporaryFileFactory temporaryFileFactory = new TemporaryFileFactory(settings);
            nodeIStorage = new MemoryMappedFileEnumerableData<>(serializer, temporaryFileFactory, settings);
            innerReferencesStorage = new MemoryMappedFileEnumerableData<>(new LongSerializer(), temporaryFileFactory, settings);
        } else {
            throw new RuntimeException(String.format("Unknown DataFactoryType '%s'", dataFactoryType));
        }

        ITreeNodeBuilder<CartesianTreeNode> cartesianTreeNodeBuilder = new CartesianTreeNodeBuilder(nodeIStorage);
        IHeapKeyResolver heapKeyResolver = new RandomHeapKeyResolver(new RandomGeneratorFactory());

        INodeAllocator<CartesianTreeNode> nodeAllocator = new NodeAllocator<>(nodeIStorage, innerReferencesStorage, new FreeNodesSet());

        ITreeNodeProvider<CartesianTreeNode> nodeProvider = new TreeNodeProvider<>(nodeIStorage, cartesianTreeNodeBuilder, nodeAllocator);
        return new CartesianTreeManager(nodeProvider, heapKeyResolver);
    }
}
