package compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public class FindingSearcher implements IFindingSearcher
{
    public IEdge search(String text, IReadableCharArray string, INode node, int position)
    {
        char needCharacter = string.get(position);
        if (node == null)
            return null;
        if (node.getEdges().containsKey(needCharacter))
            return node.getEdges().get(needCharacter);
        return null;
    }
}
