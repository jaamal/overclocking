package tests.unit.CompressionService.lz77;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;
import compressionservice.compression.algorithms.lz77.windows.IStringWindow;
import compressionservice.compression.algorithms.lz77.windows.StringWindow;

public class StringWindowTest extends UnitTestBase
{
    private IStringWindow stringWindow;
    private IPlace place;

    @Test
    public void notSuccess()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("bbb");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("aaa"));

        Assert.assertEquals(0, this.place.getPosition());
        Assert.assertEquals(0, this.place.getLength());
    }

    @Test
    public void equalString()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("aaaa"));

        Assert.assertEquals(0, this.place.getPosition());
        Assert.assertEquals(3, this.place.getLength());
    }

    @Test
    public void test4()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("aaa"));

        Assert.assertEquals(6, this.place.getPosition());
        Assert.assertEquals(3, this.place.getLength());
    }

    @Test
    public void test5()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        this.stringWindow.append("ccc");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("aaa"));

        Assert.assertEquals(7, this.place.getPosition());
        Assert.assertEquals(2, this.place.getLength());
    }

    @Test
    public void test6()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        this.stringWindow.append("ccc");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("aacceee"));

        Assert.assertEquals(7, this.place.getPosition());
        Assert.assertEquals(4, this.place.getLength());
    }

    @Test
    public void textNullText()
    {
        this.stringWindow = new StringWindow(5);
        this.place = this.stringWindow.search(new MemoryReadableCharArray("aaa"));

        Assert.assertEquals(0, this.place.getPosition());
        Assert.assertEquals(0, this.place.getLength());
    }

    @Test
    public void test7()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        this.stringWindow.append("ccc");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("cacceee"));

        Assert.assertEquals(9, this.place.getPosition());
        Assert.assertEquals(1, this.place.getLength());
    }

    @Test
    public void test8()
    {
        this.stringWindow = new StringWindow(20);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        this.stringWindow.append("ccc");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("cacceee"));

        Assert.assertEquals(9, this.place.getPosition());
        Assert.assertEquals(1, this.place.getLength());
    }

    @Test
    public void test9()
    {
        this.stringWindow = new StringWindow(20);
        this.stringWindow.append("aaasss");
        this.stringWindow.append("bbbzzz");
        this.stringWindow.append("aaaaaa");
        this.stringWindow.append("cccasdjk");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("cacceee"));

        Assert.assertEquals(20, this.place.getPosition());
        Assert.assertEquals(2, this.place.getLength());
    }

    @Test
    public void test10()
    {
        this.stringWindow = new StringWindow(20);
        this.stringWindow.append("");
        this.stringWindow.append("");
        this.stringWindow.append("cccasdjk");
        this.stringWindow.append("");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("cacceee"));

        Assert.assertEquals(2, this.place.getPosition());
        Assert.assertEquals(2, this.place.getLength());
    }

    @Test
    public void test11()
    {
        this.stringWindow = new StringWindow(10);
        this.stringWindow.append("aaa");
        this.stringWindow.append("aaa");
        this.stringWindow.append("cccasdjkaa");
        this.place = this.stringWindow.search(new MemoryReadableCharArray("cccasdjkaaa"));

        Assert.assertEquals(6, this.place.getPosition());
        Assert.assertEquals(10, this.place.getLength());
    }
}
