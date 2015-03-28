package caching.lineNumberMappers;

import java.util.HashMap;

public class LineNumberMapper implements ILineNumberMapper
{
    private final HashMap<Long, Integer> byConnectionLineNumber;
    private final HashMap<Integer, Long> byCacheLineNumber;

    public LineNumberMapper()
    {
        byConnectionLineNumber = new HashMap<Long, Integer>();
        byCacheLineNumber = new HashMap<Integer, Long>();
    }

    @Override
    public boolean isEmpty()
    {
        return byConnectionLineNumber.isEmpty();
    }

    @Override
    public boolean containsCacheLineNumber(int cacheLineNumber)
    {
        return byCacheLineNumber.containsKey(cacheLineNumber);
    }

    @Override
    public boolean containsConnectionLineNumber(long connectionLineNumber)
    {
        return byConnectionLineNumber.containsKey(connectionLineNumber);
    }

    @Override
    public long toConnectionLineNumber(int cacheLineNumber)
    {
        return byCacheLineNumber.get(cacheLineNumber);
    }

    @Override
    public int toCacheLineNumber(long connectionLineNumber)
    {
        return byConnectionLineNumber.get(connectionLineNumber);
    }

    @Override
    public void removeByCacheLineNumber(int cacheLineNumber)
    {
        if (containsCacheLineNumber(cacheLineNumber))
        {
            long connectionLineNumber = toConnectionLineNumber(cacheLineNumber);
            byCacheLineNumber.remove(cacheLineNumber);
            removeByConnectionLineNumber(connectionLineNumber);
        }
    }

    @Override
    public void removeByConnectionLineNumber(long connectionLineNumber)
    {
        if (containsConnectionLineNumber(connectionLineNumber))
        {
            int cacheLineNumber = toCacheLineNumber(connectionLineNumber);
            byConnectionLineNumber.remove(connectionLineNumber);
            removeByCacheLineNumber(cacheLineNumber);
        }
    }

    @Override
    public void addMapping(int cacheLineNumber, long connectionLineNumber)
    {
        if (containsCacheLineNumber(cacheLineNumber))
            throw new DuplicateCacheLineNumberException(cacheLineNumber);
        if (containsConnectionLineNumber(connectionLineNumber))
            throw new DuplicateConnectionLineNumberException(connectionLineNumber);
        byCacheLineNumber.put(cacheLineNumber, connectionLineNumber);
        byConnectionLineNumber.put(connectionLineNumber, cacheLineNumber);
    }
}
