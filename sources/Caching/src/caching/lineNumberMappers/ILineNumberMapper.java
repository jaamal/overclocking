package caching.lineNumberMappers;

public interface ILineNumberMapper
{
    boolean isEmpty();
    boolean containsCacheLineNumber(int cacheLineNumber);
    boolean containsConnectionLineNumber(long connectionLineNumber);
    long toConnectionLineNumber(int cacheLineNumber);
    int toCacheLineNumber(long connectionLineNumber);
    void removeByCacheLineNumber(int cacheLineNumber);
    void removeByConnectionLineNumber(long connectionLineNumber);
    void addMapping(int cacheLineNumber, long connectionLineNumber);
}
