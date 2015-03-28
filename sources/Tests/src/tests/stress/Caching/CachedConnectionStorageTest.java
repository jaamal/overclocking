package tests.stress.Caching;

import junit.framework.Assert;

import org.junit.Test;

import tests.stress.StressTestBase;
import caching.ByteArrayStorageFactory;
import caching.CachedConnectionStorage;
import caching.configuration.ICacheConfiguration;
import caching.connections.FileConnectionFactory;
import caching.connections.ITemporaryFileFactory;
import caching.doubleLinkedList.ListFactory;
import caching.doubleLinkedList.ListNodeProviderFactory;
import caching.lineNumberMappers.LineNumberMapperFactory;
import caching.victimSelectors.LRUVictimSelectorFactory;

public class CachedConnectionStorageTest extends StressTestBase
{
    private CachedConnectionStorage<Integer> storage;

    @Override
    public void setUp()
    {
        super.setUp();
        IntegerSerializer serializer = new IntegerSerializer();
        ITemporaryFileFactory temporaryFileFactory = container.get(ITemporaryFileFactory.class);
        ICacheConfiguration cacheConfiguration = new ICacheConfiguration()
        {
            @Override
            public int getCacheLineCount()
            {
                return 29;
            }

            @Override
            public int getCacheLineLength()
            {
                return 16;
            }
        };
        FileConnectionFactory connectionFactory = new FileConnectionFactory(temporaryFileFactory);
        storage = new CachedConnectionStorage<Integer>(
                connectionFactory,
                cacheConfiguration,
                new LineNumberMapperFactory(),
                new LRUVictimSelectorFactory(new ListFactory(new ListNodeProviderFactory())),
                new ByteArrayStorageFactory<Integer>(serializer),
                serializer);
    }

    @Test
    public void test()
    {
        final int count = 1056;
        for (int i = 0; i < count; ++i)
            storage.save(i, i);
        for (int i = 0; i < count; ++i)
            Assert.assertEquals(i, (int) storage.load(i));
        for (int i = 0; i < count; ++i)
            storage.save(i, i * i);
        for (int i = 0; i < count; i += 3)
            Assert.assertEquals(i * i, (int)storage.load(i));
    }
}

