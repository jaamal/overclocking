package caching.doubleLinkedList;

public class ListNodeProvider implements IListNodeProvider
{
	private final ListNode[] nodes;
	private int size;

	public ListNodeProvider(int size) {
		this.nodes = new ListNode[size];
		this.size = 0;
	}

	@Override
	public ListNode get(int key)
	{
		try {
			checkExistence(key);
			return nodes[key];
		}
		catch (NodeNotCreatedYetException e) {
			return null;
		}
	}

	public ListNode create(int key)
	{
		checkCorrectness(key);

		if (nodes[key] != null)
			throw new NodeAlreadyCreatedException();

		++size;
		return nodes[key] = construct(key, key, key);
	}

	@Override
	public ListNode updatePrev(ListNode node, int prev)
	{

		checkActuality(node);
		if (node.prev == prev)
			return node;

		nodes[node.key] = construct(node.key, prev, node.next);
		updateNext(get(prev), node.key);
		return nodes[node.key];
	}

	@Override
	public ListNode updateNext(ListNode node, int next)
	{

		checkActuality(node);
		if (node.next == next)
			return node;

		nodes[node.key] = construct(node.key, node.prev, next);
		updatePrev(get(next), node.key);
		return nodes[node.key];
	}

	@Override
	public void delete(ListNode node)
	{
		checkActuality(node);

		nodes[node.key] = null;
		--size;
	}

    @Override
	public int size()
	{
		return size;
	}

	private ListNode construct(int key, int prev, int next)
	{
		checkCorrectness(prev);
		checkCorrectness(next);

		return new ListNode(key, prev, next);
	}

	private void checkCorrectness(int key)
	{
		if (key < 0 || key >= nodes.length)
			throw new InvalidKeyException(key);
	}

	private void checkExistence(int key)
	{
		checkCorrectness(key);
		if (nodes[key] == null)
			throw new NodeNotCreatedYetException(key);
	}

	private void checkActuality(ListNode node)
	{
		if (node == null)
			throw new NonActualNodeException();

		ListNode node2 = null;

		try {
			node2 = get(node.key);
		}
		catch (InvalidKeyException e) {
			throw new NonActualNodeException();
		}

		if (!node.equals(node2))
			throw new NonActualNodeException();
	}
}
