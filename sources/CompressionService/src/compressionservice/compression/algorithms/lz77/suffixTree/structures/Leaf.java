package compressionservice.compression.algorithms.lz77.suffixTree.structures;

public class Leaf implements IEdge
{
    private int beginPosition;
    private INode beginNode;
    private static int endPosition;
    private int number;

    public Leaf(int beginPosition, INode beginNode, int number)
    {
        this.beginPosition = beginPosition;
        this.beginNode = beginNode;
        this.number = number;
    }

    public static void changeEndPosition(int endPosition)
    {
        Leaf.endPosition = endPosition;
    }

    @Override
    public int beginPosition()
    {
        return this.beginPosition;
    }

    @Override
    public int endPosition()
    {
        return Leaf.endPosition;
    }

    @Override
    public INode beginNode()
    {
        return this.beginNode;
    }

    @Override
    public INode endNode()
    {
        return null;
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
