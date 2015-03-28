package cartesianTree.exceptions;

public class InvalidRangeException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InvalidRangeException(long count, long left, long right) {
		super(String.format("Invalid range [{0}, {1}). count = {2}.", left, right, count));
	}
}
