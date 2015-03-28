package compressionservice.compression.algorithms.lz77.suffixTree;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;

public interface ITree
{
    IPlace stringInformation(IReadableCharArray string);

    void append(String string);
}
