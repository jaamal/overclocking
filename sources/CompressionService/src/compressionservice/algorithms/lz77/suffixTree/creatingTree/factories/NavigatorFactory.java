package compressionservice.algorithms.lz77.suffixTree.creatingTree.factories;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.INavigator;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.Navigator;

public class NavigatorFactory implements INavigatorFactory
{
    @Override
    public INavigator create(String text, IIInsertPlaceFactory insertPlaceFactory)
    {
        return new Navigator(text, insertPlaceFactory);
    }

}
