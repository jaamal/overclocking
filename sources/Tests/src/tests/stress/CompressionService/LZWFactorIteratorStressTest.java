package tests.stress.CompressionService;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import tests.stress.StressTestBase;
import compressingCore.dataAccess.IDataFactory;
import compressionservice.algorithms.lzw.LZWFactorIterator;
import data.charArray.IReadableCharArray;

public class LZWFactorIteratorStressTest extends StressTestBase
{
    @Test
    public void testStress()
    {
        LZWFactorIterator lzwFactorIterator = new LZWFactorIterator(createHugeRandomCharArray());
        long[] result = extractAllFactorCodes(lzwFactorIterator);
        System.out.println(result.length);
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

    private IReadableCharArray createHugeRandomCharArray()
    {
        Random random = new Random();
        ArrayList<Character> arrayList = new ArrayList<Character>();
        final int length = 1024 * 1024;
        for (int index = 0; index < length; index++)
            arrayList.add((char) ((int) 'a' + random.nextInt(4)));
        char[] result = new char[arrayList.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = arrayList.get(i);
        return container.get(IDataFactory.class).createCharArray(result);
    }
}
