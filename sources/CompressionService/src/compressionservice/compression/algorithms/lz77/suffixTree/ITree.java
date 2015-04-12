package compressionservice.compression.algorithms.lz77.suffixTree;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Location;

public interface ITree
{
    Location stringInformation(IReadableCharArray string);

    void append(String string);
}
