package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public interface IBeginPlaceFactory
{
    IBeginPlace create(INode node, int begin, int end);
}
