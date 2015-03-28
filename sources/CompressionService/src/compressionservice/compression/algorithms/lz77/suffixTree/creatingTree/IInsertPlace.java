package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public interface IInsertPlace
{
    IEdge getEdge();

    int getPosition();

    INode getNode();

    void changeEdge(IEdge edge);
}
