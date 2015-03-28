package tests.integration.CompressionService.lzInf;

import helpers.FileHelpers;
import helpers.TestHelpers;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import tests.integration.IntegrationTestBase;

import compressingCore.dataAccess.IReadableCharArray;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.compression.algorithms.lzInf.ILZFactorIterator;
import compressionservice.compression.algorithms.lzInf.LZFactor;
import compressionservice.compression.algorithms.lzInf.LZFactorIteratorFactory;

import dataContracts.DataFactoryType;

public class LZFactorIteratorIntegrationTest extends IntegrationTestBase
{
    private LZFactorIteratorFactory lzFactorIteratorFactory;

    @Override
    public void setUp()
    {
        super.setUp();
        lzFactorIteratorFactory = container.get(LZFactorIteratorFactory.class);
    }

    @Test
    public void testOneSymbol()
    {
        doTest("a");
    }

    @Test
    public void testOneSymbolString()
    {
        doTest("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }

    @Test
    public void testDifferentSymbolsString()
    {
        doTest("qwertyuiopasdfghjklzxcvbnm");
    }

    @Test
    public void testString1()
    {
        doTest("xabxacxabxacxabxac");
    }

    @Test
    public void testString2()
    {
        doTest("xabxacxabxacxabxacxabxacxabxacxabxacxabxacxabxacxabxac");
    }

    @Test
    public void testString3()
    {
        doTest("aaabbbcccaaabbbccc");
    }

    @Test
    public void testSimpleDNA()
    {
        String string = FileHelpers.readTestFile("simpleDNA_clean.txt", Charset.forName("Cp1251"));
        doTest(string);
    }

    @Test
    public void testSimpleDNATwoSections()
    {
        String string = FileHelpers.readTestFile("simpleDNA_twoSections_clean.txt", Charset.forName("Cp1251"));
        doTest(string);
    }

    @Test
    public void testRandomSmallAlphabite()
    {
        String randomString = TestHelpers.generateRandomString(new Random(), 1000, 2);
        doTest(randomString);
    }

    @Test
    public void testRandomBigAlphabite()
    {
        String randomString = TestHelpers.generateRandomString(new Random(), 1000, 20);
        doTest(randomString);
    }

    @Test
    public void testRandomManyCases()
    {
        Random rnd = new Random();
        for (int i = 0; i < 100; i++)
        {
            String randomString = TestHelpers.generateRandomString(rnd, 100, 6);
            doTest(randomString);
        }
    }

    private ArrayList<LZFactor> getFactors(IReadableCharArray charArray)
    {
        ArrayList<LZFactor> factors = new ArrayList<LZFactor>();

        try(ILZFactorIterator lzFactorizator = lzFactorIteratorFactory.create(DataFactoryType.memory, charArray);)  {
            while (lzFactorizator.hasFactors())
                factors.add(lzFactorizator.getNextFactor());
        }
        return factors;
    }

    private void doTest(String string)
    {
        IReadableCharArray charArray = new MemoryReadableCharArray(string);
        ArrayList<LZFactor> factors = getFactors(charArray);
        System.out.println("Factors count = " + factors.size());
        Assert.assertEquals(string, unpack(factors));
    }

    private static String unpack(ArrayList<LZFactor> factors)
    {
        StringBuffer result = new StringBuffer();
        for (int index = 0; index < factors.size(); ++index)
        {
            LZFactor factor = factors.get(index);
            if (factor.isTerminal)
            {
                result.append(factor.value);
            }
            else
            {
                String subString = result.substring(
                        (int) factor.startPosition, (int) (factor.endPosition));
                result.append(subString);
            }
        }
        return result.toString();
    }
}
