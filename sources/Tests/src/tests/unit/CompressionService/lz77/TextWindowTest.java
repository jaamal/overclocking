package tests.unit.CompressionService.lz77;

import org.junit.Test;
import compressionservice.algorithms.lz77.TextWindow;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.MemoryDataFactory;
import junit.framework.Assert;
import tests.unit.UnitTestBase;

public class TextWindowTest extends UnitTestBase
{
    @Test
    public void notSuccess()
    {
        TextWindow textWindow = TextWindow.create(5);
        textWindow.append("bbb");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("aaa".toCharArray()));
        Assert.assertEquals(Location.create(0, 0), actual);
    }

    @Test
    public void equalString()
    {
        TextWindow textWindow = TextWindow.create(5);
        textWindow.append("aaa");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("aaaa".toCharArray()));
        Assert.assertEquals(Location.create(0, 3), actual);
    }

    @Test
    public void test4()
    {
        TextWindow textWindow = TextWindow.create(5);
        textWindow.append("aaa");
        textWindow.append("bbb");
        textWindow.append("aaa");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("aaa".toCharArray()));
        Assert.assertEquals(Location.create(6, 3), actual);
    }

    @Test
    public void test5()
    {
        TextWindow textWindow = TextWindow.create(5);
        textWindow.append("aaa");
        textWindow.append("bbb");
        textWindow.append("aaa");
        textWindow.append("ccc");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("aaa".toCharArray()));
        Assert.assertEquals(Location.create(7, 2), actual);
    }

    @Test
    public void test6()
    {
        TextWindow textWindow = TextWindow.create(5);
        textWindow.append("aaa");
        textWindow.append("bbb");
        textWindow.append("aaa");
        textWindow.append("ccc");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("aacceee".toCharArray()));
        Assert.assertEquals(Location.create(7, 4), actual);
    }

    @Test
    public void textNullText()
    {
        TextWindow textWindow = TextWindow.create(5);
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("aaa".toCharArray()));
        Assert.assertEquals(Location.create(0, 0), actual);
    }

    @Test
    public void test7()
    {
        TextWindow textWindow = TextWindow.create(5);
        textWindow.append("aaa");
        textWindow.append("bbb");
        textWindow.append("aaa");
        textWindow.append("ccc");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("cacceee".toCharArray()));
        Assert.assertEquals(Location.create(9, 1), actual);
    }

    @Test
    public void test8()
    {
        TextWindow textWindow = TextWindow.create(20);
        textWindow.append("aaa");
        textWindow.append("bbb");
        textWindow.append("aaa");
        textWindow.append("ccc");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("cacceee".toCharArray()));
        Assert.assertEquals(Location.create(9, 1), actual);
    }

    @Test
    public void test9()
    {
        TextWindow textWindow = TextWindow.create(20);
        textWindow.append("aaasss");
        textWindow.append("bbbzzz");
        textWindow.append("aaaaaa");
        textWindow.append("cccasdjk");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("cacceee".toCharArray()));
        Assert.assertEquals(Location.create(20, 2), actual);
    }

    @Test
    public void test10()
    {
        TextWindow textWindow = TextWindow.create(20);
        textWindow.append("");
        textWindow.append("");
        textWindow.append("cccasdjk");
        textWindow.append("");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("cacceee".toCharArray()));
        Assert.assertEquals(Location.create(2, 2), actual);
    }

    @Test
    public void test11()
    {
        TextWindow textWindow = TextWindow.create(10);
        textWindow.append("aaa");
        textWindow.append("aaa");
        textWindow.append("cccasdjkaa");
        Location actual = textWindow.search(new MemoryDataFactory().createCharArray("cccasdjkaaa".toCharArray()));
        Assert.assertEquals(Location.create(6, 10), actual);
    }
}
