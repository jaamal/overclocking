package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IInsertPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public class Navigator implements INavigator
{
    private IInsertPlaceFactory insertPlaceFactory;
    private String text;

    public Navigator(String text, IInsertPlaceFactory insertPlaceFactory)
    {
        this.text = text;
        this.insertPlaceFactory = insertPlaceFactory;
    }

    @Override
    public IInsertPlace getPath(IBeginPlace beginPlace, int beginPosition, int endPosition)
    {
        int begin;
        int end;
        IEdge edge;
        INode node = beginPlace.getNode();
        if (node.getFatherEdge() == null)
        {
            begin = beginPosition;
            end = endPosition;
        }
        else
        {
            node = node.getSuffixLink();
            begin = beginPlace.beginPosition();
            end = beginPlace.endPosition();
        }

        edge = node.findEdge(text.charAt(begin));
        while (end - begin > edge.toPosition() - edge.fromPosition())
        {
            begin += edge.toPosition() - edge.fromPosition() + 1;
            node = edge.toNode();
            edge = node.findEdge(text.charAt(begin));
        }
        int index = 0;
        while (begin + index < end && this.text.charAt(begin + index) == this.text.charAt(edge.fromPosition() + index))
            ++index;
        return this.insertPlaceFactory.create(node, edge, index);
    }

}
