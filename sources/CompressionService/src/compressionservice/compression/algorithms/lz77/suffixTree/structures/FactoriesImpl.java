package compressionservice.compression.algorithms.lz77.suffixTree.structures;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.AppenderFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.BeginPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IAppenderFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IIInsertPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IInsertPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.INavigatorFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixLinkerFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.NavigatorFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.SearcherFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.SuffixLinkerFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.SuffixPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories.FinderFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories.FindingSearcherFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories.IFinderFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories.IFindingSearcherFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.EdgeFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.INodeFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.NodeFactory;


public class FactoriesImpl implements IFactories
{
    private INodeFactory nodeFactory;
    private IEdgeFactory edgeFactory;
    private INavigatorFactory navigatorFactory;
    private IAppenderFactory appenderFactory;
    private IBeginPlaceFactory beginPlaceFactory;
    private IIInsertPlaceFactory insertPlaceFactory;
    private ISearcherFactory searcherFactory;
    private ISuffixLinkerFactory suffixLinkerFactory;
    private ISuffixPlaceFactory suffixPlaceFactory;
    private IFinderFactory finderFactory;
    private IFindingSearcherFactory findingSearcherFactory;

    public FactoriesImpl()
    {
        this.nodeFactory = new NodeFactory();
        this.edgeFactory = new EdgeFactory();
        this.navigatorFactory = new NavigatorFactory();
        this.appenderFactory = new AppenderFactory();
        this.beginPlaceFactory = new BeginPlaceFactory();
        this.insertPlaceFactory = new IInsertPlaceFactory();
        this.searcherFactory = new SearcherFactory();
        this.suffixLinkerFactory = new SuffixLinkerFactory();
        this.suffixPlaceFactory = new SuffixPlaceFactory();
        this.finderFactory = new FinderFactory();
        this.findingSearcherFactory = new FindingSearcherFactory();
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
    public IIInsertPlaceFactory getInsertPlaceFactory()
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

    @Override
    public IFinderFactory getFinderFactory()
    {
        return this.finderFactory;
    }

    @Override
    public IFindingSearcherFactory getFindingSearcherFactory()
    {
        return this.findingSearcherFactory;
    }
}
