package compressionservice.algorithms.lz77.suffixTree.structures;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IAppenderFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IInsertPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.INavigatorFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixLinkerFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.factories.IFinderFactory;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.factories.IFindingSearcherFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.INodeFactory;

public interface IFactories
{
    INodeFactory getNodeFactory();

    IEdgeFactory getEdgeFactory();

    INavigatorFactory getNavigatorFactory();

    IAppenderFactory getAppenderFactory();

    IBeginPlaceFactory getBeginPlaceFactory();

    IInsertPlaceFactory getInsertPlaceFactory();

    ISearcherFactory getSearcherFactory();

    ISuffixLinkerFactory getSuffixLinkerFactory();

    ISuffixPlaceFactory getSuffixPlaceFactory();

    IFinderFactory getFinderFactory();

    IFindingSearcherFactory getFindingSearcherFactory();
}
