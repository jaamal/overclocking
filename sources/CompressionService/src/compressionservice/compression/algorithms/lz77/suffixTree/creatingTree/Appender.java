package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.INodeFactory;

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
        if (insertPlace.getEdge().beginPosition() + insertPlace.getPosition() == insertPlace.getEdge().endPosition())
        {
            if (insertPlace.getEdge().endNode() != null)
            {
                IEdge edge = searcher.search(text, insertPlace.getEdge().endNode(), numberOfChar);
                if (edge == null)
                    insertPlace.getEdge().endNode().addEdge(text.charAt(numberOfChar), this.edgeFactory.createLeaf(numberOfChar, insertPlace.getEdge().endNode(), edgeNumber));
                else
                    this.isExtension = true;
            }
            return null;
        }

        if (insertPlace.getEdge().beginPosition() + insertPlace.getPosition() < insertPlace.getEdge().endPosition()
                && text.charAt(insertPlace.getEdge().beginPosition() + insertPlace.getPosition() + 1) != text.charAt(numberOfChar))
        {

            INode node = this.nodeFactory.create(insertPlace.getEdge().getNumber());
            IEdge newEdge = this.edgeFactory.createEdge(insertPlace.getEdge().beginPosition(), insertPlace.getEdge().beginNode(), insertPlace
                    .getEdge().beginPosition()
                    + insertPlace.getPosition(), node, insertPlace.getEdge().getNumber());
            IEdge edge = this.edgeFactory.create(insertPlace.getEdge(), insertPlace.getEdge().beginPosition() + insertPlace.getPosition() + 1, node,
                    insertPlace.getEdge().getNumber());

            node.setFatherEdge(newEdge);
            node.addEdge(text.charAt(insertPlace.getEdge().beginPosition() + insertPlace.getPosition() + 1), edge);
            node.addEdge(text.charAt(numberOfChar), this.edgeFactory.createLeaf(numberOfChar, node, edgeNumber));

            insertPlace.getEdge().beginNode().changeEdge(text.charAt(insertPlace.getEdge().beginPosition()), newEdge);
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
            node.addEdge(text.charAt(numberOfChar), this.edgeFactory.createLeaf(numberOfChar, node, numberOfChar));
        else
            this.isExtension = true;
    }

    @Override
    public boolean isImplicitExtension()
    {
        return this.isExtension;
    }

}
