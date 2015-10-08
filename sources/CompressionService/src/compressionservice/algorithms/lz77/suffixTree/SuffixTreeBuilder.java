package compressionservice.algorithms.lz77.suffixTree;

import compressionservice.algorithms.lz77.suffixTree.searchingInTree.IFinder;
import compressionservice.algorithms.lz77.suffixTree.structures.IFactories;

public class SuffixTreeBuilder implements ISuffixTreeBuilder
{
    private final IFactories factories;
    private final IFinder finder;

    public SuffixTreeBuilder(IFactories factories, IFinder finder) {
        this.factories = factories;
        this.finder = finder;
    }
    
    @Override
    public ISuffixTree build(String text)
    {
        SuffixTree result = new SuffixTree(factories.getNodeFactory(), factories.getEdgeFactory(), factories.getSuffixPlaceFactory(),
                factories.getBeginPlaceFactory(), factories.getInsertPlaceFactory(), factories.getNavigatorFactory(), 
                factories.getSearcherFactory().create(), factories.getSuffixLinkerFactory().create(factories.getSuffixPlaceFactory()), 
                factories.getAppenderFactory().create(factories.getEdgeFactory(), factories.getNodeFactory()), finder);
        result.append(text);
        return result;
    }
}
