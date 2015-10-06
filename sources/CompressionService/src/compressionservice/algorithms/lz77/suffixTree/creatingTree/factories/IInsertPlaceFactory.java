package compressionservice.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public interface IInsertPlaceFactory
{
    IInsertPlace create(INode currentNode, IEdge currentEdge, int position);
}
