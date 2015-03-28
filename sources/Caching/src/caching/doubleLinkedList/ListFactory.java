package caching.doubleLinkedList;

public class ListFactory implements IListFactory
{
    private IListNodeProviderFactory listNodeProviderFactory;

    public ListFactory(IListNodeProviderFactory listNodeProviderFactory)
    {
        this.listNodeProviderFactory = listNodeProviderFactory;
    }

    @Override
	public IList create(int maxSize)
	{
		return new List(listNodeProviderFactory, maxSize);
	}
}
