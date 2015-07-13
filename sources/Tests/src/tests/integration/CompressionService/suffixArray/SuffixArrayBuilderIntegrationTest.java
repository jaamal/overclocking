package tests.integration.CompressionService.suffixArray;

import static org.junit.Assert.assertEquals;
import helpers.TestHelpers;

import org.junit.Test;

import tests.integration.IntegrationTestBase;
import compressionservice.algorithms.lzInf.suffixArray.ISuffixArray;
import compressionservice.algorithms.lzInf.suffixArray.SuffixArrayBuilder;
import data.IDataFactory;
import dataContracts.DataFactoryType;

public class SuffixArrayBuilderIntegrationTest extends IntegrationTestBase
{
    private SuffixArrayBuilder suffixArrayBuilder;

    @Override
    public void setUp()
    {
        super.setUp();
        
        suffixArrayBuilder = container.get(SuffixArrayBuilder.class);
    }

    @Test
    public void testOneSymbol()
    {
        ISuffixArray suffixArray = suffixArrayBuilder.build(DataFactoryType.memory, container.get(IDataFactory.class).createCharArray("a".toCharArray()));
        doAssert(new long[]{0}, suffixArray);
        suffixArray.dispose();
    }

    @Test
    public void testAaaaa()
    {
        ISuffixArray suffixArray = suffixArrayBuilder.build(DataFactoryType.memory, container.get(IDataFactory.class).createCharArray("aaaaa".toCharArray()));
        doAssert(new long[]{4, 3, 2, 1, 0}, suffixArray);
        suffixArray.dispose();
    }

    @Test
    public void testMissisipi()
    {
        ISuffixArray suffixArray = suffixArrayBuilder.build(DataFactoryType.memory, container.get(IDataFactory.class).createCharArray("missisipi".toCharArray()));
        doAssert(new long[]{8, 6, 4, 1, 0, 7, 5, 3, 2}, suffixArray);
        suffixArray.dispose();
    }
    
    @Test
    public void testHugeString()
    {
        String hugeStr = TestHelpers.genString(32 * 1024, 4);
        ISuffixArray suffixArray = suffixArrayBuilder.build(DataFactoryType.memory, container.get(IDataFactory.class).createCharArray(hugeStr.toCharArray()));
        assertEquals(hugeStr.length(), suffixArray.length());
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
