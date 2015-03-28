package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.BeginPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public class BeginPlaceFactory implements IBeginPlaceFactory
{
    @Override
    public IBeginPlace create(INode node, int begin, int end)
    {
        return new BeginPlace(node, begin, end);
    }

}
