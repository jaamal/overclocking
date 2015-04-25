package compressionservice.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISuffixLinker;

public interface ISuffixLinkerFactory
{
    ISuffixLinker create(ISuffixPlaceFactory suffixPlaceFactory);
}
