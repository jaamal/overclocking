package compressionservice.compression.algorithms.lz77.windows;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Location;

public interface IStringWindow
{
    Location search(IReadableCharArray string);

    void append(String string);
}
