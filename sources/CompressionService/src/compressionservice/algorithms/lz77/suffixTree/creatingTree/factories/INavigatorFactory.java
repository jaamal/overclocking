package compressionservice.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.INavigator;

public interface INavigatorFactory
{
    public INavigator create(String text, IIInsertPlaceFactory insertPlaceFactory);
}
