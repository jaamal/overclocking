package compressionservice.algorithms.lz77.suffixTree;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.IAppender;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.INavigator;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISearcher;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISuffixLinker;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISuffixPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IIInsertPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.INavigatorFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.IFinder;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.factories.IFinderFactory;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.factories.IFindingSearcherFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.IFactories;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.Leaf;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.INodeFactory;
import data.charArray.IReadableCharArray;

public class SuffixTree implements ISuffixTree
{
    private INode root;
    private IEdge firstLeaf;
    private String text;
    private INodeFactory nodeFactory;
    private final IEdgeFactory edgeFactory;
    private final INavigatorFactory navigatorFactory;
    private final IBeginPlaceFactory beginPlaceFactory;
    private final IIInsertPlaceFactory insertPlaceFactory;
    private final ISuffixPlaceFactory suffixPlaceFactory;
    private final IFindingSearcherFactory findingSearcherFactory;
    private final IFinderFactory finderFactory;

    private int phaseIndex;
    private int extension;
    private ISearcher searcher;
    private INavigator navigator;
    private ISuffixLinker suffixLinker;
    private IAppender appender;
    private final String emptyString = "";

    public SuffixTree(String text, IFactories factories)
    {
        this.nodeFactory = factories.getNodeFactory();
        this.edgeFactory = factories.getEdgeFactory();
        this.suffixPlaceFactory = factories.getSuffixPlaceFactory();
        this.beginPlaceFactory = factories.getBeginPlaceFactory();
        this.insertPlaceFactory = factories.getInsertPlaceFactory();
        this.navigatorFactory = factories.getNavigatorFactory();
        this.searcher = factories.getSearcherFactory().create();
        this.suffixLinker = factories.getSuffixLinkerFactory().create(this.suffixPlaceFactory);
        this.appender = factories.getAppenderFactory().create(this.edgeFactory, this.nodeFactory);
        this.finderFactory = factories.getFinderFactory();
        this.findingSearcherFactory = factories.getFindingSearcherFactory();

        this.text = text;
        this.phaseIndex = 0;
        this.extension = 0;

        createTree();
    }

    private void createTree()
    {
        this.root = this.nodeFactory.create();
        this.firstLeaf = this.edgeFactory.createLeaf(0, root, 0);
        this.root.putEdge(text.charAt(0), this.firstLeaf);

        append(this.emptyString);
    }

    @Override
    public void append(String string)
    {
        this.text += string;
        this.navigator = this.navigatorFactory.create(this.text, this.insertPlaceFactory);

        for (; this.phaseIndex < this.text.length() - 1; ++this.phaseIndex)
        {
            IBeginPlace beginPlace = this.beginPlaceFactory.create(this.firstLeaf.fromNode(), this.firstLeaf.fromPosition(), this.phaseIndex);
            ISuffixPlace suffixPlace = this.suffixPlaceFactory.create(-1, this.root);
            boolean isImplicitExtension = false;

            int extensionIndex = this.extension + 1;
            for (; extensionIndex < this.phaseIndex + 1 && !isImplicitExtension; ++extensionIndex)
            {
                IInsertPlace insertPlace = this.navigator.getPath(beginPlace, extensionIndex, this.phaseIndex);
                INode node = this.appender.append(this.text, insertPlace, this.phaseIndex + 1, extensionIndex);
                isImplicitExtension = this.appender.isImplicitExtension();
                suffixPlace = this.suffixLinker.createSuffixLink(suffixPlace, node, insertPlace, extensionIndex);
                beginPlace = this.searcher.searchEnd(insertPlace.getEdge(), insertPlace.getPosition(), this.beginPlaceFactory);
            }
            this.appender.append(this.text, this.root, this.phaseIndex + 1);
            Leaf.changeEndPosition(this.phaseIndex + 1);
            isImplicitExtension = this.appender.isImplicitExtension();
            this.extension = isImplicitExtension ? extensionIndex - 2 : this.phaseIndex;
            while (this.firstLeaf.toNode() != null)
                this.firstLeaf = firstLeaf.toNode().findEdge(text.charAt(this.firstLeaf.toPosition() + 1));
        }
    }

    @Override
    public Location search(IReadableCharArray string)
    {
        IFinder finder = this.finderFactory.create(this.text, string, this.findingSearcherFactory.create());
        return finder.search(this.root);
    }

}
