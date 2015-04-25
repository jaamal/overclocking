package compressionservice.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISearcher;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.Searcher;

public class SearcherFactory implements ISearcherFactory
{
    public ISearcher create()
    {
        return new Searcher();
    }
}
