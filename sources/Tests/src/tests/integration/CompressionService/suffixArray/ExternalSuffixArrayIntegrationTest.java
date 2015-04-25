package tests.integration.CompressionService.suffixArray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tests.integration.IntegrationTestBase;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.algorithms.lzInf.suffixArray.ISuffixArray;
import compressionservice.algorithms.lzInf.suffixArray.SuffixArrayBuilder;
import dataContracts.DataFactoryType;

public class ExternalSuffixArrayIntegrationTest extends IntegrationTestBase
{
    private SuffixArrayBuilder suffixArrayFactory;

    @Override
    public void setUp()
    {
        super.setUp();
        
        suffixArrayFactory = container.get(SuffixArrayBuilder.class);
    }

    @Test
    public void testOneSymbol()
    {
        ISuffixArray suffixArray = suffixArrayFactory.build(DataFactoryType.memory, new MemoryReadableCharArray("a"));
        doAssert(new long[]{0}, suffixArray);
        suffixArray.dispose();
    }

    @Test
    public void testAaaaa()
    {
        ISuffixArray suffixArray = suffixArrayFactory.build(DataFactoryType.memory, new MemoryReadableCharArray("aaaaa"));
        doAssert(new long[]{4, 3, 2, 1, 0}, suffixArray);
        suffixArray.dispose();
    }

    @Test
    public void testMissisipi()
    {
        ISuffixArray suffixArray = suffixArrayFactory.build(DataFactoryType.memory, new MemoryReadableCharArray("missisipi"));
        doAssert(new long[]{8, 6, 4, 1, 0, 7, 5, 3, 2}, suffixArray);
        suffixArray.dispose();
    }

    private static void doAssert(long[] expected, ISuffixArray actual)
    {
        assertEquals(expected.length, actual.length());
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(expected[i], actual.get(i));
        }
    }
}
