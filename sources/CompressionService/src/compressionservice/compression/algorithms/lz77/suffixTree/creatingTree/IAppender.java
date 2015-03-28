package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public interface IAppender
{
    public INode append(String text, IInsertPlace insertPlace, int numberOfChar, int edgeNumber);

    public void append(String text, INode node, int numberOfChar);

    public boolean isImplicitExtension();
}
