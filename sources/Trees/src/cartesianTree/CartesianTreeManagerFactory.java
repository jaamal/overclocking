package cartesianTree;

import tree.nodeProviders.INodeAllocator;
import tree.nodeProviders.ITreeNodeBuilder;
import tree.nodeProviders.ITreeNodeProvider;
import tree.nodeProviders.NodeAllocator;
import tree.nodeProviders.TreeNodeProvider;
import tree.nodeProviders.indexSets.FreeNodesSet;
import cartesianTree.heapKeyResolvers.IHeapKeyResolver;
import cartesianTree.heapKeyResolvers.RandomGeneratorFactory;
import cartesianTree.heapKeyResolvers.RandomHeapKeyResolver;
import cartesianTree.nodes.CartesianTreeNode;
import cartesianTree.nodes.CartesianTreeNodeBuilder;
import cartesianTree.nodes.CartesianTreeNodeSerializer;

import commons.files.FileManager;
import commons.files.IFileManager;
import commons.settings.ISettings;

import data.enumerableData.IEnumerableData;
import data.enumerableData.InMemoryEnumerableData;
import data.enumerableData.MemoryMappedFileEnumerableData;
import data.longArray.LongSerializer;
import dataContracts.DataFactoryType;

public class CartesianTreeManagerFactory implements ICartesianTreeManagerFactory {
    private DataFactoryType dataFactoryType;
    private ISettings settings;

    public CartesianTreeManagerFactory(ISettings settings, DataFactoryType dataFactoryType) {
        this.settings = settings;
        this.dataFactoryType = dataFactoryType;
    }

    @Override
    public ICartesianTreeManager create() {
        IEnumerableData<CartesianTreeNode> nodeStorage;
        IEnumerableData<Long> innerReferencesStorage;
        if (dataFactoryType == DataFactoryType.memory) {
            nodeStorage = new InMemoryEnumerableData<>(CartesianTreeNode.class);
            innerReferencesStorage = new InMemoryEnumerableData<>(Long.class);
        } else if (dataFactoryType == DataFactoryType.file) {
            CartesianTreeNodeSerializer serializer = new CartesianTreeNodeSerializer();
            IFileManager fileManager = new FileManager(settings);
            nodeStorage = new MemoryMappedFileEnumerableData<>(serializer, fileManager, settings);
            innerReferencesStorage = new MemoryMappedFileEnumerableData<>(new LongSerializer(), fileManager, settings);
        } else {
            throw new RuntimeException(String.format("Unknown DataFactoryType '%s'", dataFactoryType));
        }

        ITreeNodeBuilder<CartesianTreeNode> cartesianTreeNodeBuilder = new CartesianTreeNodeBuilder(nodeStorage);
        IHeapKeyResolver heapKeyResolver = new RandomHeapKeyResolver(new RandomGeneratorFactory());

        INodeAllocator<CartesianTreeNode> nodeAllocator = new NodeAllocator<>(nodeStorage, innerReferencesStorage, new FreeNodesSet());

        ITreeNodeProvider<CartesianTreeNode> nodeProvider = new TreeNodeProvider<>(nodeStorage, cartesianTreeNodeBuilder, nodeAllocator);
        return new CartesianTreeManager(nodeProvider, heapKeyResolver);
    }
}
