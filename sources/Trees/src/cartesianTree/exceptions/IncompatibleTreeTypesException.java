package cartesianTree.exceptions;

public class IncompatibleTreeTypesException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public IncompatibleTreeTypesException() {
		super("Trees have different types");
	}
}
