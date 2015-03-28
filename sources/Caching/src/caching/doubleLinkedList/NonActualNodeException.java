package caching.doubleLinkedList;

public class NonActualNodeException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public NonActualNodeException() {
		super("Given node doesn't actual for this NodeProvider");
	}
}
