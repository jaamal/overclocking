package tests.integration.CompressionService.lz77;

import helpers.FactorizationScenarios;
import helpers.FileHelpers;
import helpers.TestHelpers;

import java.nio.charset.Charset;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import tests.integration.IntegrationTestBase;
import compressionservice.algorithms.factorization.IFactorIterator;
import compressionservice.algorithms.factorization.LZ77FactorIterator;
import compressionservice.algorithms.lz77.TextWindow;
import data.IDataFactory;
import data.charArray.IReadableCharArray;
import dataContracts.FactorDef;

public class LZ77FactorIteratorIntegrationTest extends IntegrationTestBase
{
    @Test
    public void testOneSymbol()
    {
        doTest("a", 4232);
    }

    @Test
    public void testOneSymbolString()
    {
        doTest("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 500);
    }

    @Test
    public void testDifferentSymbolsString()
    {
        doTest("qwertyuiopasdfghjklzxcvbnm", 2600);
    }

    @Test
    public void testString1()
    {
        doTest("xabxacxabxacxabxac", 160);
    }

    @Test
    public void testString2()
    {
        doTest("xabxacxabxacxabxacxabxacxabxacxabxacxabxacxabxacxabxac", 5);
    }

    @Test
    public void testString3()
    {
        doTest("aaabbbcccaaabbbccc", 10);
    }

    @Test
    public void testSimpleDNA()
    {
        String string = FileHelpers.readTestFile("simpleDNA_clean.txt", Charset.forName("Cp1251"));
        doTest(string, 17);
    }

    @Test
    public void testSimpleDNA2()
    {
        String string = FileHelpers.readTestFile("simpleDNA_clean.txt", Charset.forName("Cp1251"));
        doTest(string, 128);
    }

    @Test
    public void testSimpleDNATwoSections()
    {
        String string = FileHelpers.readTestFile("simpleDNA_twoSections_clean.txt", Charset.forName("Cp1251"));
        doTest(string, 31);
    }

    @Test
    public void testSimpleDNATwoSections2()
    {
        String string = FileHelpers.readTestFile("simpleDNA_twoSections_clean.txt", Charset.forName("Cp1251"));
        doTest(string, 128);
    }

    @Test
    public void testRandomSmallAlphabite()
    {
        String randomString = TestHelpers.genString(10000, 2);
        doTest(randomString, 119);
    }

    @Test
    public void testRandomBigAlphabite()
    {
        String randomString = TestHelpers.genString(10000, 20);
        doTest(randomString, 61);
    }

    @Test
    public void testRandomManyCases()
    {
        for (int i = 0; i < 100; i++)
        {
            String randomString = TestHelpers.genString(1000, 6);
            doTest(randomString, 35);
        }
    }

    private static FactorDef[] getFactors(IReadableCharArray charArray, int windowSize)
    {
        try(IFactorIterator factorIterator = new LZ77FactorIterator(TextWindow.create(windowSize), charArray)){
            ArrayList<FactorDef> factors = new ArrayList<FactorDef>();
            while (factorIterator.any())
                factors.add(factorIterator.next());
            return factors.toArray(new FactorDef[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doTest(String string, int windowSize)
    {
        IReadableCharArray charArray = container.get(IDataFactory.class).createCharArray(string.toCharArray());
        FactorDef[] factors = getFactors(charArray, windowSize);
        String actual = FactorizationScenarios.stringify(factors);
        Assert.assertEquals(string, actual);
    }
}
