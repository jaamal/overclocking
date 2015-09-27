package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public class Searcher implements ISearcher
{
    @Override
    public IBeginPlace searchEnd(IEdge edge, int numberOfChar, IBeginPlaceFactory beginPlaceFactory)
    {
        if (edge == null)
            return null;
        if (edge.fromNode().getSuffixLink() == null && edge.fromNode().getFatherEdge() != null)
        {
            INode node = edge.fromNode();
            IEdge newEdge = node.getFatherEdge();
            return beginPlaceFactory.create(newEdge.fromNode(), newEdge.fromPosition(), newEdge.toPosition());
        }
        return beginPlaceFactory.create(edge.fromNode(), edge.fromPosition(), edge.fromPosition() + numberOfChar);
    }
}
