package caching.victimSelectors;

import caching.doubleLinkedList.IListFactory;

public class LRUVictimSelectorFactory implements IVictimSelectorFactory
{
    private IListFactory listFactory;

    public LRUVictimSelectorFactory(IListFactory listFactory)
    {
        this.listFactory = listFactory;
    }

    @Override
    public IVictimSelector create(int cacheSize)
    {
        return new LRUVictimSelector(listFactory, cacheSize);
    }
}
