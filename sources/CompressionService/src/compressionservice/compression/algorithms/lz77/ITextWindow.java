package compressionservice.compression.algorithms.lz77;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Location;

public interface ITextWindow
{
    Location search(IReadableCharArray charArray);
    void append(String text);
    int size();
}
