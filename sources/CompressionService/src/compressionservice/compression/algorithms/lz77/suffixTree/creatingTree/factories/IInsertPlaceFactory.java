package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.InsertPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public class IInsertPlaceFactory implements IIInsertPlaceFactory
{
    public IInsertPlace create(INode currentNode, IEdge currentEdge, int position)
    {
        return new InsertPlace(currentNode, currentEdge, position);
    }

}
