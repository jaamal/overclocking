package compressionservice.algorithms.lz77.suffixTree.structures;

public interface INode
{
    public void addEdge(char symbol, IEdge edge);

    public IEdge findEdge(char symbol);

    public void setSuffixLink(INode node);

    public INode getSuffixLink();

    public IEdge getFatherEdge();

    public void changeEdge(char symbol, IEdge newEdge);

    public void setFatherEdge(IEdge edge);

}
