package compressionservice.compression.algorithms.lz77.suffixTree.structures;

public interface IEdge
{
    public int beginPosition();

    public int endPosition();

    public INode beginNode();

    public INode endNode();

    public int getNumber();

    public void setNumber(int number);
}
