package compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.IFinder;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.IFindingSearcher;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IPlaceFactory;

public interface IFinderFactory
{
    IFinder create(String text, IReadableCharArray string, IFindingSearcher findingSearcher, IPlaceFactory placeFactory);
}
