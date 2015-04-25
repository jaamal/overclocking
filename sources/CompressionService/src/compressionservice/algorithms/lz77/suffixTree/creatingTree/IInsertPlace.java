package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public interface IInsertPlace
{
    IEdge getEdge();

    int getPosition();

    INode getNode();

    void changeEdge(IEdge edge);
}
