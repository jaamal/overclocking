package caching.doubleLinkedList;

//TODO test this package (take all tests from old repository)
public class List implements IList
{
	private final IListNodeProvider nodeProvider;
	private final int headKey;

	public List(IListNodeProviderFactory listNodeProviderFactory, int maxSize) {
		headKey = maxSize;

		nodeProvider = listNodeProviderFactory.create(maxSize + 1);
		nodeProvider.create(headKey);
	}

	@Override
	public void appendToBegin(int key)
	{
		checkHeadKey(key);
		appendAfter(headKey, nodeProvider.create(key));
	}

	@Override
	public void delete(int key)
	{
		checkHeadKey(key);
		ListNode node = nodeProvider.get(key);
		nodeProvider.updateNext(nodeProvider.get(node.prev), node.next);
		nodeProvider.delete(node);
	}

	@Override
	public void moveToBegin(int key)
	{
		delete(key);
		appendToBegin(key);
	}

	@Override
	public int size()
	{
		return nodeProvider.size() - 1;
	}

	private void appendAfter(int prevKey, ListNode newNode)
	{
		int nextKey = nodeProvider.get(prevKey).next;

		newNode = nodeProvider.updateNext(newNode, nextKey);
		nodeProvider.updatePrev(newNode, prevKey);
	}

	private void checkHeadKey(int key)
	{
		if (key == headKey)
			throw new InvalidKeyException(key);
	}

	@Override
	public int getLastKey()
	{
		ListNode head = nodeProvider.get(headKey);
		return nodeProvider.get(head.prev).key;
	}
}
