package caching.connections;

public class ConnectionNotOpenedYetException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ConnectionNotOpenedYetException() {
		super("Connection doesn't opened yet");
	}

}
