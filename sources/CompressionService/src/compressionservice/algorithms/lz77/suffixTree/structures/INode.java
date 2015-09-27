package compressionservice.algorithms.lz77.suffixTree.structures;

public interface INode
{
    public void putEdge(char symbol, IEdge edge);

    public IEdge findEdge(char symbol);

    public void setSuffixLink(INode node);

    public INode getSuffixLink();

    public IEdge getFatherEdge();

    public void setFatherEdge(IEdge edge);

}
