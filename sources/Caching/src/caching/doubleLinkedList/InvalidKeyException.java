package caching.doubleLinkedList;

public class InvalidKeyException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InvalidKeyException(int key) {
		super("Invalid key " + key);
	}

}
