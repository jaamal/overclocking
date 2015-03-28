package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.Appender;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.IAppender;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.INodeFactory;

public class AppenderFactory implements IAppenderFactory
{

    @Override
    public IAppender create(ISearcherFactory searcherFactory,
                            IEdgeFactory edgeFactory, INodeFactory nodeFactory)
    {
        return new Appender(searcherFactory, edgeFactory, nodeFactory);
    }
}
