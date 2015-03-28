package compressionservice.compression.algorithms.lz77.suffixTree.structures.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Node;

public class NodeFactory implements INodeFactory
{
    @Override
    public INode create(int number)
    {
        return new Node(number);
    }

}
