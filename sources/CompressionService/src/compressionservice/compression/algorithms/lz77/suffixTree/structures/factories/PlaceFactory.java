package compressionservice.compression.algorithms.lz77.suffixTree.structures.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Place;

public class PlaceFactory implements IPlaceFactory
{
    @Override
    public IPlace create(long position, long length)
    {
        return new Place(position, length);
    }

}
