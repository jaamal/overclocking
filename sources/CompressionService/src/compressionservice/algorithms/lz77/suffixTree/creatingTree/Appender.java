package compressionservice.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.INodeFactory;

public class Appender implements IAppender
{
    private ISearcherFactory searcherFactory;
    private IEdgeFactory edgeFactory;
    private INodeFactory nodeFactory;
    private boolean isExtension;

    public Appender(ISearcherFactory searcherFactory, IEdgeFactory edgeFactory, INodeFactory nodeFactory)
    {
        this.searcherFactory = searcherFactory;
        this.edgeFactory = edgeFactory;
        this.nodeFactory = nodeFactory;
        this.isExtension = false;
    }

    @Override
    public INode append(String text, IInsertPlace insertPlace, int numberOfChar, int edgeNumber)
    {
        this.isExtension = false;
        ISearcher searcher = this.searcherFactory.create();
        if (insertPlace.getEdge().fromPosition() + insertPlace.getPosition() == insertPlace.getEdge().toPosition())
        {
            if (insertPlace.getEdge().toNode() != null)
            {
                IEdge edge = searcher.search(text, insertPlace.getEdge().toNode(), numberOfChar);
                if (edge == null)
                    insertPlace.getEdge().toNode().putEdge(text.charAt(numberOfChar), this.edgeFactory.createLeaf(numberOfChar, insertPlace.getEdge().toNode(), edgeNumber));
                else
                    this.isExtension = true;
            }
            return null;
        }

        if (insertPlace.getEdge().fromPosition() + insertPlace.getPosition() < insertPlace.getEdge().toPosition()
                && text.charAt(insertPlace.getEdge().fromPosition() + insertPlace.getPosition() + 1) != text.charAt(numberOfChar))
        {

            INode node = this.nodeFactory.create();
            IEdge newEdge = this.edgeFactory.createEdge(insertPlace.getEdge().fromPosition(), insertPlace.getEdge().fromNode(), insertPlace
                    .getEdge().fromPosition()
                    + insertPlace.getPosition(), node, insertPlace.getEdge().getNumber());
            IEdge edge = this.edgeFactory.create(insertPlace.getEdge(), insertPlace.getEdge().fromPosition() + insertPlace.getPosition() + 1, node,
                    insertPlace.getEdge().getNumber());

            node.setFatherEdge(newEdge);
            node.putEdge(text.charAt(insertPlace.getEdge().fromPosition() + insertPlace.getPosition() + 1), edge);
            node.putEdge(text.charAt(numberOfChar), this.edgeFactory.createLeaf(numberOfChar, node, edgeNumber));

            insertPlace.getEdge().fromNode().putEdge(text.charAt(insertPlace.getEdge().fromPosition()), newEdge);
            insertPlace.changeEdge(newEdge);
            return node;
        }

        this.isExtension = true;
        return null;
    }

    @Override
    public void append(String text, INode node, int numberOfChar)
    {
        this.isExtension = false;
        ISearcher searcher = this.searcherFactory.create();
        IEdge edge = searcher.search(text, node, numberOfChar);
        if (edge == null)
            node.putEdge(text.charAt(numberOfChar), this.edgeFactory.createLeaf(numberOfChar, node, numberOfChar));
        else
            this.isExtension = true;
    }

    @Override
    public boolean isImplicitExtension()
    {
        return this.isExtension;
    }

}
