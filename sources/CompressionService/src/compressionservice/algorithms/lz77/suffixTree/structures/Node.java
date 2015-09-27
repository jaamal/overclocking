package compressionservice.algorithms.lz77.suffixTree.structures;

import java.util.HashMap;

public class Node implements INode
{
    private HashMap<Character, IEdge> edges;
    private INode suffixLink;
    private IEdge fatherEdge;

    public Node()
    {
        this.edges = new HashMap<Character, IEdge>();
    }

    @Override
    public void putEdge(char symbol, IEdge edge)
    {
        this.edges.put(symbol, edge);
    }

    @Override
    public IEdge findEdge(char symbol)
    {
        return edges.containsKey(symbol) 
                ? edges.get(symbol) 
                : null;
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
    public void setFatherEdge(IEdge fatherEdge)
    {
        this.fatherEdge = fatherEdge;
    }
}
