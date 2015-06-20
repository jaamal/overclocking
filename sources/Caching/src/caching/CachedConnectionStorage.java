package caching;

import caching.configuration.ICacheConfiguration;
import caching.connections.IConnection;
import caching.connections.IConnectionFactory;
import caching.lineNumberMappers.ILineNumberMapper;
import caching.lineNumberMappers.ILineNumberMapperFactory;
import caching.serializers.ISerializer;
import caching.victimSelectors.IVictimSelector;
import caching.victimSelectors.IVictimSelectorFactory;

public class CachedConnectionStorage<T> implements IStorage<T>
{
    private final int cacheLineInBytes;
    private final int objectInCacheLine;
    private final int objectLength;
    private IConnection connection;
    private final IVictimSelector victimSelector;
    private final ByteArrayStorage<T>[] cacheLines;
    private final IByteArrayStorageFactory byteArrayStorageFactory;
    private final ILineNumberMapper lineNumberMapper;

    public CachedConnectionStorage(
            IConnectionFactory connectionFactory,
            ICacheConfiguration cacheConfiguration,
            ILineNumberMapperFactory lineNumberMapperFactory,
            IVictimSelectorFactory victimSelectorFactory,
            IByteArrayStorageFactory byteArrayStorageFactory,
            ISerializer<T> serializer)
    {
        this.connection = connectionFactory.create();
        this.byteArrayStorageFactory = byteArrayStorageFactory;
        victimSelector = victimSelectorFactory.create(cacheConfiguration.getCacheLineCount());
        objectInCacheLine = cacheConfiguration.getCacheLineLength();
        objectLength = serializer.sizeInBytes();
        cacheLineInBytes = objectInCacheLine * objectLength;
        lineNumberMapper = lineNumberMapperFactory.create();
        cacheLines = new ByteArrayStorage[cacheConfiguration.getCacheLineCount()];

        connection.open();
    }

    @Override
    public T load(long number)
    {
        if (number == -1)
            return null;
        checkIndex(number);
        return getCacheLine(number / objectInCacheLine).load(number % objectInCacheLine);
    }

    @Override
    public void save(long number, T obj)
    {
        checkIndex(number);
        getCacheLine(number / objectInCacheLine).save(number % objectInCacheLine, obj);
    }

    private static void checkIndex(long number)
    {
        if (number < 0)
            throw new IndexOutOfBoundsException();
    }

    @Override
    public void close()
    {
        clearCache();
        connection.close();
    }

    @Override
    protected void finalize() throws Throwable
    {
        close();
        super.finalize();
    }

    private void clearCache()
    {
        while (!lineNumberMapper.isEmpty())
        {
            freeCacheLine(victimSelector.selectVictimAndFixUsage());
        }
    }

    private ByteArrayStorage<T> getCacheLine(long number)
    {
        if (lineNumberMapper.containsConnectionLineNumber(number))
        {
            int cacheLineNumber = lineNumberMapper.toCacheLineNumber(number);
            victimSelector.fixUsage(cacheLineNumber);
            return cacheLines[cacheLineNumber];
        }
        int cacheLineNumber = victimSelector.selectVictimAndFixUsage();
        freeCacheLine(cacheLineNumber);
        lineNumberMapper.addMapping(cacheLineNumber, number);
        return cacheLines[cacheLineNumber] = byteArrayStorageFactory.create(connection.read(number * cacheLineInBytes, cacheLineInBytes));
    }

    private void freeCacheLine(int cacheLineNumber)
    {
        if (cacheLines[cacheLineNumber] != null)
        {
            long connectionLineNumber = lineNumberMapper.toConnectionLineNumber(cacheLineNumber);
            connection.write(connectionLineNumber * cacheLineInBytes, cacheLines[cacheLineNumber].getData());
            cacheLines[cacheLineNumber] = null;
            lineNumberMapper.removeByCacheLineNumber(cacheLineNumber);
        }
    }
}
