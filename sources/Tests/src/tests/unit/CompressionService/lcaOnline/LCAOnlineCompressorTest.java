package tests.unit.CompressionService.lcaOnline;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import avlTree.slpBuilders.SLPBuilder;
import tests.unit.UnitTestBase;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.algorithms.lcaOnlineSlp.LCAOnlineCompressor;
import dataContracts.SLPStatistics;

public class LCAOnlineCompressorTest extends UnitTestBase{
    @Override
    public void setUp() {
        super.setUp();

        compressor = new LCAOnlineCompressor();
    }

    @Test
    public void testEmpty()
    {
        doTest("");
    }

    @Test
    public void test1()
    {
        doTest("a");
    }

    @Test
    public void test2()
    {
        doTest("aaaaa");
    }

    @Test
    public void test3()
    {
        doTest("abacaba");
    }

    @Test
    public void test4()
    {
        doTest(getRandomString(1000000));
    }

    private static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i)
        {
            builder.append((char)(random.nextInt(4) + 'a'));
        }
        return builder.toString();
    }


    private void doTest(String text) {
        SLPBuilder slp = compressor.buildSLP(new MemoryReadableCharArray(text));
        SLPStatistics statistics = slp.getStatistics();
        System.out.println(String.format("Length = %d, count of rules = %d, height = %d", text.length(), statistics.countRules, statistics.height));
        Assert.assertEquals(text, slp.getProductString());
    }

    private LCAOnlineCompressor compressor;
}
