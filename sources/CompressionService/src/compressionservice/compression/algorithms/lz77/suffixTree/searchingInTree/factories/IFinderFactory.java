package compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.IFinder;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.IFindingSearcher;

public interface IFinderFactory
{
    IFinder create(String text, IReadableCharArray string, IFindingSearcher findingSearcher);
}
