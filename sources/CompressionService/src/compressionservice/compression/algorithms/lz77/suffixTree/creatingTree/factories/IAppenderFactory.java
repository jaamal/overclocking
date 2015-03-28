package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.IAppender;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.INodeFactory;

public interface IAppenderFactory
{
    IAppender create(ISearcherFactory searcherFactory, IEdgeFactory edgeFactory, INodeFactory nodeFactory);
}
