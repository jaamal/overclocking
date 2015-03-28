package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.ISuffixLinker;

public interface ISuffixLinkerFactory
{
    ISuffixLinker create(ISuffixPlaceFactory suffixPlaceFactory);
}
