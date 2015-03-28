package caching.doubleLinkedList;

public class ListNode
{
	public final int key;
	public final int prev;
	public final int next;

	public ListNode(int key, int prev, int next) {
		this.key = key;
		this.prev = prev;
		this.next = next;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ListNode) {
			ListNode node = (ListNode) obj;
			return key == node.key && prev == node.prev && next == node.next;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		Long[] fields = new Long[] { (long) key, (long) prev, (long) next };
		return fields.hashCode();
	}
}
