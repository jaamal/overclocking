package compressionservice.algorithms.lz77.suffixTree.searchingInTree;

import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import data.charArray.IReadableCharArray;

public class FindingSearcher implements IFindingSearcher
{
    public IEdge search(String text, IReadableCharArray string, INode node, int position)
    {
        if (node == null)
            return null;
        return node.findEdge(string.get(position));
    }
}
