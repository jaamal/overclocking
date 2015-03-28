package caching.serializers;

public class InvalidOffsetOfArrayException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InvalidOffsetOfArrayException() {
		super("Invalid offset in serializer");
	}
}
