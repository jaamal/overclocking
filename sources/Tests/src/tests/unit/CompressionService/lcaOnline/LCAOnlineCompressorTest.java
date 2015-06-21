package tests.unit.CompressionService.lcaOnline;

import helpers.TestHelpers;

import org.junit.Assert;
import org.junit.Test;

import tests.unit.UnitTestBase;
import compressionservice.algorithms.lcaOnlineSlp.LCAOnlineCompressor;
import data.MemoryDataFactory;
import dataContracts.SLPModel;
import dataContracts.SLPStatistics;

public class LCAOnlineCompressorTest extends UnitTestBase{
    private LCAOnlineCompressor compressor;
    
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
        doTest(TestHelpers.genString(1000000));
    }

    private void doTest(String text) {
        SLPModel slpModel = compressor.buildSLP(new MemoryDataFactory().createCharArray(text.toCharArray())).toSLPModel();
        SLPStatistics statistics = slpModel.calcStats();
        System.out.println(String.format("Length = %d, count of rules = %d, height = %d", text.length(), statistics.countRules, statistics.height));
        Assert.assertEquals(text, slpModel.toString());
    }
}
