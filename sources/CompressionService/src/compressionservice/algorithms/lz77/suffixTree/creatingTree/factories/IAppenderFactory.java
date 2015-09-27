package compressionservice.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.IAppender;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.INodeFactory;

public interface IAppenderFactory
{
    IAppender create(IEdgeFactory edgeFactory, INodeFactory nodeFactory);
}
