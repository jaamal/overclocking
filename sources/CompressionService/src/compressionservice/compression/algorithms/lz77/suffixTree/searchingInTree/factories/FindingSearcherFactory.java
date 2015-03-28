package compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.FindingSearcher;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.IFindingSearcher;

public class FindingSearcherFactory implements IFindingSearcherFactory
{
    @Override
    public IFindingSearcher create()
    {
        return new FindingSearcher();
    }

}
