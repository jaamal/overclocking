package compressionservice.algorithms.lz77.suffixTree;

import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.charArray.IReadableCharArray;

public interface ISuffixTree
{
    void append(String string);
    Location search(IReadableCharArray string);
}
