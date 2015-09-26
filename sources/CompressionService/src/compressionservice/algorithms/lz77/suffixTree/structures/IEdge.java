package compressionservice.algorithms.lz77.suffixTree.structures;

public interface IEdge
{
    public int fromPosition();

    public int toPosition();

    public INode fromNode();

    public INode toNode();

    public int getNumber();
}
