package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.INavigator;

public interface INavigatorFactory
{
    public INavigator create(String text, IIInsertPlaceFactory insertPlaceFactory, ISearcherFactory searcherFactory);
}
