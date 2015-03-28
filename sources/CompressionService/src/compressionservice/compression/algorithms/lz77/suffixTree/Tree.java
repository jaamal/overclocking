package compressionservice.compression.algorithms.lz77.suffixTree;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.IAppender;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.INavigator;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.ISearcher;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.ISuffixLinker;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.ISuffixPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IAppenderFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IIInsertPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.INavigatorFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixLinkerFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.IFinder;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories.IFinderFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories.IFindingSearcherFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IFactories;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Leaf;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.INodeFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IPlaceFactory;

public class Tree implements ITree
{
    private INode root;
    private IEdge firstLeaf;
    private String text;
    private INodeFactory nodeFactory;
    private final IEdgeFactory edgeFactory;
    private final INavigatorFactory navigatorFactory;
    private final IAppenderFactory appenderFactory;
    private final IBeginPlaceFactory beginPlaceFactory;
    private final IIInsertPlaceFactory insertPlaceFactory;
    private final ISearcherFactory searcherFactory;
    private final ISuffixLinkerFactory suffixLinkerFactory;
    private final ISuffixPlaceFactory suffixPlaceFactory;
    private final IFindingSearcherFactory findingSearcherFactory;
    private final IFinderFactory finderFactory;
    private final IPlaceFactory placeFactory;

    private int phaseIndex;
    private int extension;
    private ISearcher searcher;
    private INavigator navigator;
    private ISuffixLinker suffixLinker;
    private IAppender appender;
    private final String emptyString = "";

    public Tree(String text, IFactories factories)
    {
        this.nodeFactory = factories.getNodeFactory();
        this.edgeFactory = factories.getEdgeFactory();
        this.navigatorFactory = factories.getNavigatorFactory();
        this.appenderFactory = factories.getAppenderFactory();
        this.beginPlaceFactory = factories.getBeginPlaceFactory();
        this.insertPlaceFactory = factories.getInsertPlaceFactory();
        this.searcherFactory = factories.getSearcherFactory();
        this.suffixLinkerFactory = factories.getSuffixLinkerFactory();
        this.suffixPlaceFactory = factories.getSuffixPlaceFactory();
        this.finderFactory = factories.getFinderFactory();
        this.findingSearcherFactory = factories.getFindingSearcherFactory();
        this.placeFactory = factories.getPlaceFactory();

        this.text = text;
        this.phaseIndex = 0;
        this.extension = 0;

        createTree();
    }

    private void createTree()
    {
        this.root = this.nodeFactory.create(0);
        this.firstLeaf = this.edgeFactory.createLeaf(0, root, 0);
        this.root.addEdge(text.charAt(0), this.firstLeaf);

        this.searcher = this.searcherFactory.create();

        this.suffixLinker = this.suffixLinkerFactory.create(this.suffixPlaceFactory);
        this.appender = this.appenderFactory.create(this.searcherFactory, this.edgeFactory, this.nodeFactory);

        append(this.emptyString);
    }

    @Override
    public void append(String string)
    {
        this.text += string;
        this.navigator = this.navigatorFactory.create(this.text, this.insertPlaceFactory, this.searcherFactory);

        for (; this.phaseIndex < this.text.length() - 1; ++this.phaseIndex)
        {
            IBeginPlace beginPlace = this.beginPlaceFactory.create(this.firstLeaf.beginNode(), this.firstLeaf.beginPosition(), this.phaseIndex);
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
            while (this.firstLeaf.endNode() != null)
                this.firstLeaf = this.searcher.search(this.text, this.firstLeaf.endNode(), this.firstLeaf.endPosition() + 1);
        }
    }

    @Override
    public IPlace stringInformation(IReadableCharArray string)
    {
        IFinder finder = this.finderFactory.create(this.text, string, this.findingSearcherFactory.create(), this.placeFactory);
        return finder.search(this.root);
    }

}
