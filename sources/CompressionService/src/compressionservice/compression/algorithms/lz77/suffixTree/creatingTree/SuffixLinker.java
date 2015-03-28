package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;


public class SuffixLinker implements ISuffixLinker
{
    private ISuffixPlaceFactory suffixPlaceFactory;

    public SuffixLinker(ISuffixPlaceFactory suffixPlaceFactory)
    {
        this.suffixPlaceFactory = suffixPlaceFactory;
    }

    @Override
    public ISuffixPlace createSuffixLink(ISuffixPlace suffixPlace, INode node, IInsertPlace insertPlace, int currentIndex)
    {
        if (suffixPlace.extension() == currentIndex - 1)
        {
            if (node == null)
                suffixPlace.getNode().setSuffixLink(insertPlace.getEdge().endNode());
            else
                suffixPlace.getNode().setSuffixLink(node);
        }
        if (node != null)
        {
            if (insertPlace.getNode().getFatherEdge() == null && node.getFatherEdge().endPosition() - node.getFatherEdge().beginPosition() == 0)
                node.setSuffixLink(insertPlace.getNode());
            return this.suffixPlaceFactory.create(currentIndex, node);
        }
        return suffixPlace;
    }

    @Override
    public void createSuffixLink(INode node, INode root, int currentIndex)
    {
        if (node != null)
            node.setSuffixLink(root);
    }

}
