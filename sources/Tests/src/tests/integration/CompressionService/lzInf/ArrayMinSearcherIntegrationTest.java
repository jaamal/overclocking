package tests.integration.CompressionService.lzInf;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import tests.integration.IntegrationTestBase;
import compressingCore.dataAccess.MemoryLongArray;
import compressionservice.compression.algorithms.lzInf.arrayMinSearching.ArrayMinSearcherFactory;
import compressionservice.compression.algorithms.lzInf.arrayMinSearching.IArrayMinSearcher;
import dataContracts.DataFactoryType;

public class ArrayMinSearcherIntegrationTest extends IntegrationTestBase
{
    private ArrayMinSearcherFactory arrayMinSearcherFactory;

    @Override
    public void setUp()
    {
        super.setUp();
        arrayMinSearcherFactory = container.get(ArrayMinSearcherFactory.class);
    }

    @Test
    public void testRandom()
    {
        doTest(0, 8472384L);
        doTest(1, 4893945L);
        doTest(10, 483983L);
        doTest(20, 893483L);
        doTest(100, 72384L);
        doTest(1000, 32323L);
    }

    private void doTest(long[] array)
    {
        IArrayMinSearcher minSearcher = arrayMinSearcherFactory.createSearcher(DataFactoryType.memory, new MemoryLongArray(array));
        for (int startIndex = 0; startIndex < array.length; startIndex++)
        {
            long currentMin = Long.MAX_VALUE;
            for (int endIndex = startIndex + 1; endIndex <= array.length; endIndex++)
            {
                currentMin = Math.min(currentMin, array[endIndex - 1]);
                Assert.assertEquals(currentMin, minSearcher.getMin(startIndex, endIndex));
            }
        }
        minSearcher.dispose();
    }

    private static long[] generateRandomArray(int size, long seed)
    {
        long[] result = new long[size];
        Random rnd = new Random(seed);
        for (int i = 0; i < result.length; i++)
            result[i] = rnd.nextLong();
        return result;
    }

    private void doTest(int size, long seed)
    {
        doTest(generateRandomArray(size, seed));
    }
}
