package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public interface ISuffixPlace
{
    int extension();

    INode getNode();

    void setExtension(int extencion);

    void setSuffixNode(INode node);
}