package compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.Finder;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.IFinder;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.IFindingSearcher;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IPlaceFactory;

public class FinderFactory implements IFinderFactory
{
    @Override
    public IFinder create(String text, IReadableCharArray string, IFindingSearcher findingSearcher, IPlaceFactory placeFactory)
    {
        return new Finder(text, string, findingSearcher, placeFactory);
    }
}
