package compressionservice.algorithms.lz77.suffixTree.structures.factories;

import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public interface IEdgeFactory
{
    public IEdge createLeaf(int beginPosition, INode beginNode, int number);

    public IEdge createEdge(int beginPosition, INode beginNode, int endPosition, INode endNode, int number);

    public IEdge create(IEdge fatherEdge, int beginPosition, INode beginNode, int number);
}
