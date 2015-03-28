package caching.doubleLinkedList;

public class NodeNotCreatedYetException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public NodeNotCreatedYetException(int key) {
		super("Node with key " + key + " not created yet");
	}

}
