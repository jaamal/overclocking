package compressionservice.compression.algorithms.lz77.suffixTree.structures.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public interface INodeFactory
{
    public INode create(int number);
}
