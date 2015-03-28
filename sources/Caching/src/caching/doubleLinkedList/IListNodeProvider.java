package caching.doubleLinkedList;

public interface IListNodeProvider
{
	ListNode get(int key);

	ListNode create(int key);

	ListNode updatePrev(ListNode node, int prev);

	ListNode updateNext(ListNode node, int next);

	void delete(ListNode node);

	int size();
}
