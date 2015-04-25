package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public class BeginPlace implements IBeginPlace
{
    private INode node;
    private int beginPosition;
    private int endPosition;

    public BeginPlace(INode node, int begin, int end)
    {
        this.node = node;
        this.beginPosition = begin;
        this.endPosition = end;
    }

    @Override
    public INode getNode()
    {
        return node;
    }

    @Override
    public int beginPosition()
    {
        return beginPosition;
    }

    @Override
    public int endPosition()
    {
        return endPosition;
    }

}
