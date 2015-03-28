package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.ISuffixPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public interface ISuffixPlaceFactory
{
    ISuffixPlace create(int extension, INode node);
}
