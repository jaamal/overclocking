package compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Location;

public interface IFinder
{
    Location search(INode root);
}
