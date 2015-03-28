package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;

public class Searcher implements ISearcher
{

    public IEdge search(String text, INode node, int position)
    {
        char needCharacter = text.charAt(position);
        if (node.getEdges().containsKey(needCharacter))
            return node.getEdges().get(needCharacter);
        return null;
    }


    @Override
    public IBeginPlace searchEnd(IEdge edge, int numberOfChar, IBeginPlaceFactory beginPlaceFactory)
    {
        if (edge == null)
            return null;
        if (edge.beginNode().getSuffixLink() == null && edge.beginNode().getFatherEdge() != null)
        {
            INode node = edge.beginNode();
            IEdge newEdge = node.getFatherEdge();
            return beginPlaceFactory.create(newEdge.beginNode(), newEdge.beginPosition(), newEdge.endPosition());
        }
        return beginPlaceFactory.create(edge.beginNode(), edge.beginPosition(), edge.beginPosition() + numberOfChar);
    }
}
