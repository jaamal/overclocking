package compressionservice.compression.algorithms.lz77.suffixTree.structures;

import java.util.HashMap;

public interface INode
{
    public void addEdge(char symbol, IEdge edge);

    public HashMap<Character, IEdge> getEdges();

    public void setSuffixLink(INode node);

    public INode getSuffixLink();

    public IEdge getFatherEdge();

    public void changeEdge(char symbol, IEdge newEdge);

    public void setFatherEdge(IEdge edge);

    public int getNumberOfEdges();

}
