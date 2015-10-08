package compressionservice.algorithms.lz77.suffixTree;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.IAppender;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.INavigator;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISearcher;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISuffixLinker;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISuffixPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IInsertPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.INavigatorFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.IFinder;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.Leaf;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.INodeFactory;
import data.charArray.IReadableCharArray;

public class SuffixTree implements ISuffixTree
{
    private final INodeFactory nodeFactory;
    private final IEdgeFactory edgeFactory;
    private final INavigatorFactory navigatorFactory;
    private final IBeginPlaceFactory beginPlaceFactory;
    private final IInsertPlaceFactory insertPlaceFactory;
    private final ISuffixPlaceFactory suffixPlaceFactory;
    private final IFinder finder;
    private final ISuffixLinker suffixLinker;
    private final IAppender appender;
    private final ISearcher searcher;

    private INode root;
    private IEdge firstLeaf;
    private String text;
    private int phaseIndex;
    private int extension;
    private INavigator navigator;

    public SuffixTree(
            INodeFactory nodeFactory,
            IEdgeFactory edgeFactory,
            ISuffixPlaceFactory suffixPlaceFactory,
            IBeginPlaceFactory beginPlaceFactory,
            IInsertPlaceFactory insertPlaceFactory,
            INavigatorFactory navigatorFactory,
            ISearcher searcher,
            ISuffixLinker suffixLinker,
            IAppender appender,
            IFinder finder)
    {
        this.nodeFactory = nodeFactory;
        this.edgeFactory = edgeFactory;
        this.suffixPlaceFactory = suffixPlaceFactory;
        this.beginPlaceFactory = beginPlaceFactory;
        this.insertPlaceFactory = insertPlaceFactory;
        this.navigatorFactory = navigatorFactory;
        this.searcher = searcher;
        this.suffixLinker = suffixLinker;
        this.appender = appender;
        this.finder = finder;

        this.text = "";
        this.phaseIndex = 0;
        this.extension = 0;
    }

    @Override
    public void append(String string)
    {
        if (string == null || string.length() == 0)
            return;
        
        text += string;
        navigator = navigatorFactory.create(text, insertPlaceFactory);
        
        if (root == null) {
            root = nodeFactory.create();
            firstLeaf = edgeFactory.createLeaf(0, root, 0);
            root.putEdge(text.charAt(0), firstLeaf);
        }

        for (; phaseIndex < text.length() - 1; ++phaseIndex)
        {
            IBeginPlace beginPlace = beginPlaceFactory.create(firstLeaf.fromNode(), firstLeaf.fromPosition(), phaseIndex);
            ISuffixPlace suffixPlace = suffixPlaceFactory.create(-1, root);
            boolean isImplicitExtension = false;

            int extensionIndex = extension + 1;
            for (; extensionIndex < phaseIndex + 1 && !isImplicitExtension; ++extensionIndex)
            {
                IInsertPlace insertPlace = navigator.getPath(beginPlace, extensionIndex, phaseIndex);
                INode node = appender.append(text, insertPlace, phaseIndex + 1, extensionIndex);
                isImplicitExtension = appender.isImplicitExtension();
                suffixPlace = suffixLinker.createSuffixLink(suffixPlace, node, insertPlace, extensionIndex);
                beginPlace = searcher.searchEnd(insertPlace.getEdge(), insertPlace.getPosition(), beginPlaceFactory);
            }
            appender.append(text, root, phaseIndex + 1);
            Leaf.changeEndPosition(phaseIndex + 1);
            isImplicitExtension = appender.isImplicitExtension();
            extension = isImplicitExtension ? extensionIndex - 2 : phaseIndex;
            while (firstLeaf.toNode() != null)
                firstLeaf = firstLeaf.toNode().findEdge(text.charAt(firstLeaf.toPosition() + 1));
        }
    }

    @Override
    public Location search(IReadableCharArray string)
    {
        return finder.search(root, text, string);
    }

}
