package caching.doubleLinkedList;

public class ListNodeProviderFactory implements IListNodeProviderFactory
{
	@Override
	public IListNodeProvider create(int maxSize)
	{
		return new ListNodeProvider(maxSize);
	}

}
