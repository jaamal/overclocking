package caching.serializers;

public class InvalidLengthOfArrayException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InvalidLengthOfArrayException(int expected, int found) {
		super("Invalid length of array. Expected: " + expected + ", found: " + found);
	}
}
