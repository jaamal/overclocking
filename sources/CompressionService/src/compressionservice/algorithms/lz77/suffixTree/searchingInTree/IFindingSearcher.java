package compressionservice.algorithms.lz77.suffixTree.searchingInTree;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public interface IFindingSearcher
{
    IEdge search(String text, IReadableCharArray string, INode node, int position);
}
