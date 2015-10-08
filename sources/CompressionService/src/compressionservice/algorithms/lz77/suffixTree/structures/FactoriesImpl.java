package compressionservice.algorithms.lz77.suffixTree.structures;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.AppenderFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.BeginPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IAppenderFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IInsertPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.INavigatorFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixLinkerFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.InsertPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.NavigatorFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.SearcherFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.SuffixLinkerFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.SuffixPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.EdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.INodeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.NodeFactory;


public class FactoriesImpl implements IFactories
{
    private INodeFactory nodeFactory;
    private IEdgeFactory edgeFactory;
    private INavigatorFactory navigatorFactory;
    private IAppenderFactory appenderFactory;
    private IBeginPlaceFactory beginPlaceFactory;
    private IInsertPlaceFactory insertPlaceFactory;
    private ISearcherFactory searcherFactory;
    private ISuffixLinkerFactory suffixLinkerFactory;
    private ISuffixPlaceFactory suffixPlaceFactory;

    public FactoriesImpl()
    {
        this.nodeFactory = new NodeFactory();
        this.edgeFactory = new EdgeFactory();
        this.navigatorFactory = new NavigatorFactory();
        this.appenderFactory = new AppenderFactory();
        this.beginPlaceFactory = new BeginPlaceFactory();
        this.insertPlaceFactory = new InsertPlaceFactory();
        this.searcherFactory = new SearcherFactory();
        this.suffixLinkerFactory = new SuffixLinkerFactory();
        this.suffixPlaceFactory = new SuffixPlaceFactory();
    }

    @Override
    public INodeFactory getNodeFactory()
    {
        return this.nodeFactory;
    }

    @Override
    public IEdgeFactory getEdgeFactory()
    {
        return this.edgeFactory;
    }

    @Override
    public INavigatorFactory getNavigatorFactory()
    {
        return this.navigatorFactory;
    }

    @Override
    public IAppenderFactory getAppenderFactory()
    {
        return this.appenderFactory;
    }

    @Override
    public IBeginPlaceFactory getBeginPlaceFactory()
    {
        return this.beginPlaceFactory;
    }

    @Override
    public IInsertPlaceFactory getInsertPlaceFactory()
    {
        return this.insertPlaceFactory;
    }

    @Override
    public ISearcherFactory getSearcherFactory()
    {
        return this.searcherFactory;
    }

    @Override
    public ISuffixLinkerFactory getSuffixLinkerFactory()
    {
        return this.suffixLinkerFactory;
    }

    @Override
    public ISuffixPlaceFactory getSuffixPlaceFactory()
    {
        return this.suffixPlaceFactory;
    }
}
