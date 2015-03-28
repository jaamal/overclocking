package compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;

public interface IFinder
{
    IPlace search(INode root);
}
