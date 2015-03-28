package caching.doubleLinkedList;

public interface IListNodeProviderFactory
{
	IListNodeProvider create(int maxSize);
}
