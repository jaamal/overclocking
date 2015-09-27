package compressionservice.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.Appender;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IAppender;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.INodeFactory;

public class AppenderFactory implements IAppenderFactory
{

    @Override
    public IAppender create(IEdgeFactory edgeFactory, INodeFactory nodeFactory)
    {
        return new Appender(edgeFactory, nodeFactory);
    }
}
