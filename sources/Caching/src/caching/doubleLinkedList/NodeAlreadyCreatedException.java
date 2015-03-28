package caching.doubleLinkedList;

public class NodeAlreadyCreatedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public NodeAlreadyCreatedException() {
		super("Node already created");
	}

}
