package tests.stress.Caching;

import java.io.File;
import java.io.RandomAccessFile;

import org.junit.Assert;
import org.junit.Test;

import tests.stress.StressTestBase;
import caching.IEnumerableData;
import caching.MemoryMappedFileEnumerableData;
import caching.connections.ITemporaryFileFactory;
import commons.settings.ISettings;

public class MemoryMappedFileStorageTest extends StressTestBase
{
    @Override
    public void setUp()
    {
        super.setUp();
    }

    @Test
    public void testTempFile()
    {
        MemoryMappedFileEnumerableData<Integer> storage;
        IntegerSerializer serializer = new IntegerSerializer();
        ITemporaryFileFactory temporaryFileFactory = container.get(ITemporaryFileFactory.class);
        storage = new MemoryMappedFileEnumerableData<Integer>(serializer, temporaryFileFactory, container.get(ISettings.class));

        final int count = 1056;
        for (int i = 0; i < count; ++i)
            storage.save(i, i);
        for (int i = 0; i < count; ++i)
            Assert.assertEquals(i, (int) storage.load(i));
        for (int i = 0; i < count; ++i)
            storage.save(i, i * i);
        for (int i = 0; i < count; i += 3)
            Assert.assertEquals(i * i, (int) storage.load(i));

        storage.close();
    }

    @Test
    public void TestExistsFile() throws Exception
    {
        IntegerSerializer serializer = new IntegerSerializer();
        String fileName = "WorkingDirectory\\testExistsFile.testEmptySLP";
        File rawFile = new File(fileName);
        rawFile.getParentFile().mkdirs();

        try (RandomAccessFile file = new RandomAccessFile(rawFile, "rw"))
        {
            byte[] bytes = new byte[4 * 1056];
            for (int i = 0; i < bytes.length / 4; i++)
            {
                serializer.serialize(i * i, bytes, i * 4);
            }

            file.write(bytes);
            file.close();

            for (int attempt = 0; attempt < 2; attempt++)
            {
                IEnumerableData<Integer> storage = new MemoryMappedFileEnumerableData<Integer>(serializer, rawFile, container.get(ISettings.class));

                final int count = 1056;
                for (int i = 0; i < count; ++i)
                {
                    int actual = storage.load(i);
                    Assert.assertEquals(i * i, actual);
                }

                storage.close();
                Assert.assertTrue(rawFile.exists());
            }
        }
        finally
        {
            rawFile.deleteOnExit();
        }
    }
}

