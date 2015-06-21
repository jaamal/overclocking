package compressionservice.algorithms.lz77.suffixTree;

import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.charArray.IReadableCharArray;

public interface ITree
{
    Location stringInformation(IReadableCharArray string);

    void append(String string);
}
