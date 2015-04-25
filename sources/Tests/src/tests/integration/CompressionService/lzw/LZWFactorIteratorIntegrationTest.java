package tests.integration.CompressionService.lzw;

import static org.junit.Assert.assertArrayEquals;
import helpers.FileHelpers;

import java.util.ArrayList;

import org.junit.Test;

import tests.integration.IntegrationTestBase;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.algorithms.lzw.LZWFactorIterator;

public class LZWFactorIteratorIntegrationTest extends IntegrationTestBase
{
    @Test
    public void testIntegration()
    {
        char[] charArray = {'a', 'b', 'a', 'a', 'b', 'a', 'b', 'a', 'a', 'b', 'a', 'a', 'b'};
        LZWFactorIterator lzwFactorIterator = new LZWFactorIterator(new MemoryReadableCharArray(charArray, 0, charArray.length));
        long[] expected = new long[]{
                97, 98, 97, Character.MAX_CODE_POINT + 1,
                Character.MAX_CODE_POINT + 1, Character.MAX_CODE_POINT + 3,
                Character.MAX_CODE_POINT + 2, Character.MAX_CODE_POINT + 1};
        long[] actual = extractAllFactorCodes(lzwFactorIterator);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIntegrationSimpleDNA()
    {
        byte[] fileContent = FileHelpers.readTestFile("simpleDNA_clean.txt");
        char[] charArray = new char[fileContent.length];
        for (int i = 0; i < fileContent.length; i++)
            charArray[i] = (char) fileContent[i];
        LZWFactorIterator lzwFactorIterator = new LZWFactorIterator(new MemoryReadableCharArray(charArray, 0, charArray.length));
        long[] actual = extractAllFactorCodes(lzwFactorIterator);
        System.out.println(actual.length);
    }

    @Test
    public void testIntegrationSimpleDNATwoSections()
    {
        byte[] fileContent = FileHelpers.readTestFile("simpleDNA_twoSections_clean.txt");
        char[] charArray = new char[fileContent.length];
        for (int i = 0; i < fileContent.length; i++)
            charArray[i] = (char) fileContent[i];
        LZWFactorIterator lzwFactorIterator = new LZWFactorIterator(new MemoryReadableCharArray(charArray, 0, charArray.length));
        long[] actual = extractAllFactorCodes(lzwFactorIterator);
        System.out.println(actual.length);
    }

    private static long[] extractAllFactorCodes(LZWFactorIterator factorIterator)
    {
        ArrayList<Long> actualList = new ArrayList<Long>();
        while (factorIterator.hasFactors())
            actualList.add(factorIterator.getNextFactor().code);
        long[] result = new long[actualList.size()];
        for (int i = 0; i < actualList.size(); i++)
            result[i] = actualList.get(i);
        return result;
    }
}
