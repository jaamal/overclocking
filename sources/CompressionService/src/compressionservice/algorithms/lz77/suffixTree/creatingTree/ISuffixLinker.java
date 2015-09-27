package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public interface ISuffixLinker
{
    ISuffixPlace createSuffixLink(ISuffixPlace suffixPlace, INode node, IInsertPlace insertPlace, int currentIndex);
}
