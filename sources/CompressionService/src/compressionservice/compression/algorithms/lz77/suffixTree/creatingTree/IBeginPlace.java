package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public interface IBeginPlace
{

    INode getNode();

    int beginPosition();

    int endPosition();
}
