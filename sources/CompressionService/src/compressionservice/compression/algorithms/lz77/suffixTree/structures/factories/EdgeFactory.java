package compressionservice.compression.algorithms.lz77.suffixTree.structures.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.Edge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Leaf;

public class EdgeFactory implements IEdgeFactory
{

    @Override
    public IEdge createLeaf(int beginPosition, INode beginNode, int number)
    {
        return new Leaf(beginPosition, beginNode, number);
    }

    @Override
    public IEdge createEdge(int beginPosition, INode beginNode, int endPosition, INode endNode, int number)
    {
        return new Edge(beginPosition, beginNode, endPosition, endNode, number);
    }

    @Override
    public IEdge create(IEdge fatherEdge, int beginPosition, INode beginNode, int number)
    {
        return fatherEdge.endNode() == null 
                ? new Leaf(beginPosition, beginNode, number) 
                : new Edge(beginPosition, beginNode, fatherEdge.endPosition(), fatherEdge.endNode(), number);
    }

}
