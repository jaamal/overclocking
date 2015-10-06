package compressionservice.algorithms.lz77.suffixTree;

import compressionservice.algorithms.lz77.suffixTree.structures.IFactories;

public class SuffixTreeBuilder implements ISuffixTreeBuilder
{
    private IFactories factories;

    public SuffixTreeBuilder(IFactories factories) {
        this.factories = factories;
    }
    
    @Override
    public ISuffixTree build(String text)
    {
        SuffixTree result = new SuffixTree(factories.getNodeFactory(), factories.getEdgeFactory(), factories.getSuffixPlaceFactory(),
                factories.getBeginPlaceFactory(), factories.getInsertPlaceFactory(), factories.getNavigatorFactory(), 
                factories.getSearcherFactory().create(), factories.getSuffixLinkerFactory().create(factories.getSuffixPlaceFactory()), 
                factories.getAppenderFactory().create(factories.getEdgeFactory(), factories.getNodeFactory()), 
                factories.getFinderFactory(), factories.getFindingSearcherFactory());
        result.append(text);
        return result;
    }
}
