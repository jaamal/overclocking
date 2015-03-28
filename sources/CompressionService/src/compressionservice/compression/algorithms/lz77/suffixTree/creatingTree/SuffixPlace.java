package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public class SuffixPlace implements ISuffixPlace
{
    private int extension;
    private INode suffixNode;

    public SuffixPlace(int extension, INode node)
    {
        this.extension = extension;
        this.suffixNode = node;
    }

    @Override
    public int extension()
    {
        return this.extension;
    }

    @Override
    public INode getNode()
    {
        return this.suffixNode;
    }

    @Override
    public void setExtension(int extencion)
    {
        this.extension = extencion;
    }

    @Override
    public void setSuffixNode(INode node)
    {
        this.suffixNode = node;
    }

}
