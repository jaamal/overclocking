package caching.serializers;

public class AttemptSerializeNullException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public AttemptSerializeNullException() {
		super("Cannot serialize NULL node");
	}
}
