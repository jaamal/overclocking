package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.ISuffixLinker;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.SuffixLinker;

public class SuffixLinkerFactory implements ISuffixLinkerFactory
{
    @Override
    public ISuffixLinker create(ISuffixPlaceFactory suffixPlaceFactory)
    {
        return new SuffixLinker(suffixPlaceFactory);
    }

}
