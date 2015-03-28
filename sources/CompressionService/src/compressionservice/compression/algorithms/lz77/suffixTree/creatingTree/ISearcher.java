package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public interface ISearcher
{
    IEdge search(String text, INode node, int position);

    IBeginPlace searchEnd(IEdge edge, int numberOfChar, IBeginPlaceFactory beginPlaceFactory);
}
