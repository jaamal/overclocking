package caching.lineNumberMappers;

public class DuplicateConnectionLineNumberException extends RuntimeException
{
	private static final long serialVersionUID = -9032403542637149754L;

	public DuplicateConnectionLineNumberException(long connectionLineNumber)
    {
        super(String.format("Duplicate number of connection line %d", connectionLineNumber));
    }
}
