package compressionservice.compression.algorithms.lz77.suffixTree.structures.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;

public interface IPlaceFactory
{
    IPlace create(long position, long length);
}
