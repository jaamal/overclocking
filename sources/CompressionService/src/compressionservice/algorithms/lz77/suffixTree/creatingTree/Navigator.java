package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IIInsertPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;

public class Navigator implements INavigator
{
    private IIInsertPlaceFactory insertPlaceFactory;
    private ISearcherFactory searcherFactory;
    private String text;

    public Navigator(String text, IIInsertPlaceFactory insertPlaceFactory, ISearcherFactory searcherFactory)
    {
        this.text = text;
        this.insertPlaceFactory = insertPlaceFactory;
        this.searcherFactory = searcherFactory;
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
        ISearcher searcher = this.searcherFactory.create();
        edge = searcher.search(this.text, node, begin);
        while (end - begin > edge.toPosition() - edge.fromPosition())
        {
            begin += edge.toPosition() - edge.fromPosition() + 1;
            node = edge.toNode();
            edge = searcher.search(this.text, node, begin);
        }
        int index = 0;
        while (begin + index < end && this.text.charAt(begin + index) == this.text.charAt(edge.fromPosition() + index))
            ++index;
        return this.insertPlaceFactory.create(node, edge, index);
    }

}
