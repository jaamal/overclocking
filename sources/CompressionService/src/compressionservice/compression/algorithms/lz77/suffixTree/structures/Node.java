package compressionservice.compression.algorithms.lz77.suffixTree.structures;

import java.util.HashMap;

public class Node implements INode
{
    private HashMap<Character, IEdge> edges;
    private INode suffixLink;
    private IEdge fatherEdge;

    public Node(int number)
    {
        this.edges = new HashMap<Character, IEdge>();
    }


    @Override
    public void addEdge(char symbol, IEdge edge)
    {
        this.edges.put(symbol, edge);
    }

    @Override
    public HashMap<Character, IEdge> getEdges()
    {
        return this.edges;
    }

    @Override
    public void setSuffixLink(INode node)
    {
        this.suffixLink = node;
    }

    @Override
    public INode getSuffixLink()
    {
        return this.suffixLink;
    }

    @Override
    public IEdge getFatherEdge()
    {
        return this.fatherEdge;
    }

    @Override
    public void changeEdge(char symbol, IEdge newEdge)
    {
        this.edges.remove(symbol);
        this.edges.put(symbol, newEdge);
    }

    @Override
    public void setFatherEdge(IEdge fatherEdge)
    {
        this.fatherEdge = fatherEdge;
    }

    @Override
    public int getNumberOfEdges()
    {
        return this.edges.size();
    }
}
