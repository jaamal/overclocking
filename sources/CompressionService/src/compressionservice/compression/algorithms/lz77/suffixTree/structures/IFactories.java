package compressionservice.compression.algorithms.lz77.suffixTree.structures;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IAppenderFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.IIInsertPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.INavigatorFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixLinkerFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories.IFinderFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.factories.IFindingSearcherFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.INodeFactory;

public interface IFactories
{
    INodeFactory getNodeFactory();

    IEdgeFactory getEdgeFactory();

    INavigatorFactory getNavigatorFactory();

    IAppenderFactory getAppenderFactory();

    IBeginPlaceFactory getBeginPlaceFactory();

    IIInsertPlaceFactory getInsertPlaceFactory();

    ISearcherFactory getSearcherFactory();

    ISuffixLinkerFactory getSuffixLinkerFactory();

    ISuffixPlaceFactory getSuffixPlaceFactory();

    IFinderFactory getFinderFactory();

    IFindingSearcherFactory getFindingSearcherFactory();
}
