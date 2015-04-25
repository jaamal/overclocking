package compressionservice.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.BeginPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public class BeginPlaceFactory implements IBeginPlaceFactory
{
    @Override
    public IBeginPlace create(INode node, int begin, int end)
    {
        return new BeginPlace(node, begin, end);
    }

}
