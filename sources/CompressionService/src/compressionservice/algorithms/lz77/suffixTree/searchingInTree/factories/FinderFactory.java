package compressionservice.algorithms.lz77.suffixTree.searchingInTree.factories;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.Finder;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.IFinder;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.IFindingSearcher;

public class FinderFactory implements IFinderFactory
{
    @Override
    public IFinder create(String text, IReadableCharArray string, IFindingSearcher findingSearcher)
    {
        return new Finder(text, string, findingSearcher);
    }
}
