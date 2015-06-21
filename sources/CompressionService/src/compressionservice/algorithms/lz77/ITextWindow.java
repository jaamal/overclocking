package compressionservice.algorithms.lz77;

import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.charArray.IReadableCharArray;

public interface ITextWindow
{
    Location search(IReadableCharArray charArray);
    void append(String text);
    int size();
}
