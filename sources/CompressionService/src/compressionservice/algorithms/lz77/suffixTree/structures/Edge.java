package compressionservice.algorithms.lz77.suffixTree.structures;

public class Edge implements IEdge
{
    private int beginPosition;
    private int endPosition;
    private INode beginNode;
    private INode endNode;
    private int number;

    public Edge(int beginPosition, INode beginNode, int endPosition, INode endNode, int number)
    {
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
        this.beginNode = beginNode;
        this.endNode = endNode;
        this.number = number;
    }

    @Override
    public int beginPosition()
    {
        return this.beginPosition;
    }

    @Override
    public int endPosition()
    {
        return this.endPosition;
    }

    @Override
    public INode beginNode()
    {
        return this.beginNode;
    }

    @Override
    public INode endNode()
    {
        return this.endNode;
    }

    @Override
    public int getNumber()
    {
        return this.number;
    }

    @Override
    public void setNumber(int number)
    {
        this.number = number;
    }


}
