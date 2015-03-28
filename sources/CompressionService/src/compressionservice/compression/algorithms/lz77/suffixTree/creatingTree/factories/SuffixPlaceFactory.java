package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.ISuffixPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.SuffixPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public class SuffixPlaceFactory implements ISuffixPlaceFactory
{
    @Override
    public ISuffixPlace create(int extension, INode node)
    {
        return new SuffixPlace(extension, node);
    }

}
