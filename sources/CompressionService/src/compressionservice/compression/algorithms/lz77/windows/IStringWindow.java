package compressionservice.compression.algorithms.lz77.windows;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;

public interface IStringWindow
{
    IPlace search(IReadableCharArray string);

    void append(String string);
}
