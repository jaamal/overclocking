package tests.unit.CompressionService.lz77;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;

import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Location;
import compressionservice.compression.algorithms.lz77.windows.IStringWindow;
import compressionservice.compression.algorithms.lz77.windows.StringWindow;

public class StringWindowTest extends UnitTestBase
{
    private IStringWindow stringWindow;

    @Test
    public void notSuccess()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("bbb");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("aaa"));
        Assert.assertEquals(Location.create(0, 0), actual);
    }

    @Test
    public void equalString()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("aaaa"));
        Assert.assertEquals(Location.create(0, 3), actual);
    }

    @Test
    public void test4()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("aaa"));
        Assert.assertEquals(Location.create(6, 3), actual);
    }

    @Test
    public void test5()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        this.stringWindow.append("ccc");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("aaa"));
        Assert.assertEquals(Location.create(7, 2), actual);
    }

    @Test
    public void test6()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        this.stringWindow.append("ccc");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("aacceee"));
        Assert.assertEquals(Location.create(7, 4), actual);
    }

    @Test
    public void textNullText()
    {
        this.stringWindow = new StringWindow(5);
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("aaa"));
        Assert.assertEquals(Location.create(0, 0), actual);
    }

    @Test
    public void test7()
    {
        this.stringWindow = new StringWindow(5);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        this.stringWindow.append("ccc");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("cacceee"));
        Assert.assertEquals(Location.create(9, 1), actual);
    }

    @Test
    public void test8()
    {
        this.stringWindow = new StringWindow(20);
        this.stringWindow.append("aaa");
        this.stringWindow.append("bbb");
        this.stringWindow.append("aaa");
        this.stringWindow.append("ccc");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("cacceee"));
        Assert.assertEquals(Location.create(9, 1), actual);
    }

    @Test
    public void test9()
    {
        this.stringWindow = new StringWindow(20);
        this.stringWindow.append("aaasss");
        this.stringWindow.append("bbbzzz");
        this.stringWindow.append("aaaaaa");
        this.stringWindow.append("cccasdjk");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("cacceee"));
        Assert.assertEquals(Location.create(20, 2), actual);
    }

    @Test
    public void test10()
    {
        this.stringWindow = new StringWindow(20);
        this.stringWindow.append("");
        this.stringWindow.append("");
        this.stringWindow.append("cccasdjk");
        this.stringWindow.append("");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("cacceee"));
        Assert.assertEquals(Location.create(2, 2), actual);
    }

    @Test
    public void test11()
    {
        this.stringWindow = new StringWindow(10);
        this.stringWindow.append("aaa");
        this.stringWindow.append("aaa");
        this.stringWindow.append("cccasdjkaa");
        Location actual = this.stringWindow.search(new MemoryReadableCharArray("cccasdjkaaa"));
        Assert.assertEquals(Location.create(6, 10), actual);
    }
}
