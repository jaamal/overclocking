package caching.lineNumberMappers;

public class DuplicateCacheLineNumberException extends RuntimeException
{
	private static final long serialVersionUID = 5272684821391313203L;

	public DuplicateCacheLineNumberException(int cacheLineNumber)
    {
        super(String.format("Duplicate number of cache line %d", cacheLineNumber));
    }
}

