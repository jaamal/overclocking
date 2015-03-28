package compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.INavigator;
import compressionservice.compression.algorithms.lz77.suffixTree.creatingTree.Navigator;

public class NavigatorFactory implements INavigatorFactory
{
    @Override
    public INavigator create(String text, IIInsertPlaceFactory insertPlaceFactory, ISearcherFactory searcherFactory)
    {
        return new Navigator(text, insertPlaceFactory, searcherFactory);
    }

}
