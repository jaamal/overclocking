package compressionservice.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public interface IBeginPlaceFactory
{
    IBeginPlace create(INode node, int begin, int end);
}
