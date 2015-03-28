package caching.victimSelectors;

import caching.doubleLinkedList.IList;
import caching.doubleLinkedList.IListFactory;

public class LRUVictimSelector implements IVictimSelector
{
    private IList list;

    public LRUVictimSelector(IListFactory listFactory, int cacheSize)
    {
        list = listFactory.create(cacheSize);
        for (int i = 0; i < cacheSize; ++i)
            list.appendToBegin(i);
    }

    @Override
    public void fixUsage(int number)
    {
        list.moveToBegin(number);
    }

    @Override
    public int selectVictimAndFixUsage()
    {
        int result = list.getLastKey();
        fixUsage(result);
        return result;
    }
}
