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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + key;
        result = prime * result + next;
        result = prime * result + prev;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ListNode other = (ListNode) obj;
        if (key != other.key)
            return false;
        if (next != other.next)
            return false;
        if (prev != other.prev)
            return false;
        return true;
    }
}
