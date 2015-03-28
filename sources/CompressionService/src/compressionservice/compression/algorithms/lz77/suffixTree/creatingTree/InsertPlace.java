package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public class InsertPlace implements IInsertPlace
{
    private IEdge edge;
    private int position;
    private INode node;

    public InsertPlace(INode node, IEdge edge, int position)
    {
        this.node = node;
        this.edge = edge;
        this.position = position;
    }

    public IEdge getEdge()
    {
        return this.edge;
    }

    public int getPosition()
    {
        return this.position;
    }

    public INode getNode()
    {
        return this.node;
    }

    @Override
    public void changeEdge(IEdge edge)
    {
        this.edge = edge;
    }

}
