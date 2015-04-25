package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public interface ISearcher
{
    IEdge search(String text, INode node, int position);

    IBeginPlace searchEnd(IEdge edge, int numberOfChar, IBeginPlaceFactory beginPlaceFactory);
}
