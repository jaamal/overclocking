package compressionservice.algorithms.lz77.suffixTree.searchingInTree;

import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.charArray.IReadableCharArray;

public interface IFinder
{
    Location search(INode root, String text, IReadableCharArray pattern);
}
