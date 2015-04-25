package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public interface IBeginPlace
{

    INode getNode();

    int beginPosition();

    int endPosition();
}
