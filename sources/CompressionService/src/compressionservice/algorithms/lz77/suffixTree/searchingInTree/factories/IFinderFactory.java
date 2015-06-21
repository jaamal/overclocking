package compressionservice.algorithms.lz77.suffixTree.searchingInTree.factories;

import compressionservice.algorithms.lz77.suffixTree.searchingInTree.IFinder;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.IFindingSearcher;
import data.charArray.IReadableCharArray;

public interface IFinderFactory
{
    IFinder create(String text, IReadableCharArray string, IFindingSearcher findingSearcher);
}
