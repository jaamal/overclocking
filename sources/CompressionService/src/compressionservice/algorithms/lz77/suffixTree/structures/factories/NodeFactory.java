package compressionservice.algorithms.lz77.suffixTree.structures.factories;

import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.Node;

public class NodeFactory implements INodeFactory
{
    @Override
    public INode create()
    {
        return new Node();
    }

}
