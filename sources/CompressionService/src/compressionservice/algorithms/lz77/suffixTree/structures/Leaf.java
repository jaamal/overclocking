package compressionservice.algorithms.lz77.suffixTree.structures;

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
    public int fromPosition()
    {
        return this.beginPosition;
    }

    @Override
    public int toPosition()
    {
        return Leaf.endPosition;
    }

    @Override
    public INode fromNode()
    {
        return this.beginNode;
    }

    @Override
    public INode toNode()
    {
        return null;
    }

    @Override
    public int getNumber()
    {
        return this.number;
    }
}
