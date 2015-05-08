package tests.integration.CompressionService.lzInf;

import helpers.FactorizationHelpers;
import helpers.FileHelpers;
import helpers.TestHelpers;

import java.nio.charset.Charset;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import tests.integration.IntegrationTestBase;
import compressingCore.dataAccess.IReadableCharArray;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.algorithms.factorization.FactorIteratorFactory;
import compressionservice.algorithms.factorization.IFactorIterator;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;

public class LZFactorIteratorIntegrationTest extends IntegrationTestBase
{
    private FactorIteratorFactory factorIteratorFactory;

    @Override
    public void setUp()
    {
        super.setUp();
        factorIteratorFactory = container.get(FactorIteratorFactory.class);
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
        String randomString = TestHelpers.genString(1000, 2);
        doTest(randomString);
    }

    @Test
    public void testRandomBigAlphabite()
    {
        String randomString = TestHelpers.genString(1000, 20);
        doTest(randomString);
    }

    @Test
    public void testRandomManyCases()
    {
        for (int i = 0; i < 100; i++)
        {
            String randomString = TestHelpers.genString(100, 6);
            doTest(randomString);
        }
    }

    private ArrayList<FactorDef> getFactors(IReadableCharArray charArray)
    {
        ArrayList<FactorDef> factors = new ArrayList<FactorDef>();
        try(IFactorIterator lzFactorizator = factorIteratorFactory.createInfiniteIterator(charArray, DataFactoryType.memory);)  {
            while (lzFactorizator.any())
                factors.add(lzFactorizator.next());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return factors;
    }

    private void doTest(String string)
    {
        IReadableCharArray charArray = new MemoryReadableCharArray(string);
        ArrayList<FactorDef> factors = getFactors(charArray);
        String actual = FactorizationHelpers.toString(factors);
        Assert.assertEquals(string, actual);
    }
}
