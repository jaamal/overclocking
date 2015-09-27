package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;

public interface ISearcher
{
    IBeginPlace searchEnd(IEdge edge, int numberOfChar, IBeginPlaceFactory beginPlaceFactory);
}
