package tests.unit.Caching.lineNumberMappers;

import caching.lineNumberMappers.DuplicateCacheLineNumberException;
import caching.lineNumberMappers.DuplicateConnectionLineNumberException;
import caching.lineNumberMappers.LineNumberMapper;
import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;

public class LineNumberMapperTest extends UnitTestBase
{
    private LineNumberMapper lineNumberMapper;

    @Override
    public void setUp()
    {
        super.setUp();
        lineNumberMapper = new LineNumberMapper();
    }

    @Test
    public void testOk()
    {
        Assert.assertTrue(lineNumberMapper.isEmpty());
        Assert.assertFalse(lineNumberMapper.containsCacheLineNumber(0));
        Assert.assertFalse(lineNumberMapper.containsConnectionLineNumber(10));

        lineNumberMapper.addMapping(0, 10);
        Assert.assertFalse(lineNumberMapper.isEmpty());
        Assert.assertTrue(lineNumberMapper.containsCacheLineNumber(0));
        Assert.assertTrue(lineNumberMapper.containsConnectionLineNumber(10));
        Assert.assertEquals(10, lineNumberMapper.toConnectionLineNumber(0));
        Assert.assertEquals(0, lineNumberMapper.toCacheLineNumber(10));

        lineNumberMapper.addMapping(-2, -1);
        Assert.assertFalse(lineNumberMapper.isEmpty());
        Assert.assertTrue(lineNumberMapper.containsCacheLineNumber(0));
        Assert.assertTrue(lineNumberMapper.containsConnectionLineNumber(10));
        Assert.assertEquals(10, lineNumberMapper.toConnectionLineNumber(0));
        Assert.assertEquals(0, lineNumberMapper.toCacheLineNumber(10));
        Assert.assertTrue(lineNumberMapper.containsCacheLineNumber(-2));
        Assert.assertTrue(lineNumberMapper.containsConnectionLineNumber(-1));
        Assert.assertEquals(-1, lineNumberMapper.toConnectionLineNumber(-2));
        Assert.assertEquals(-2, lineNumberMapper.toCacheLineNumber(-1));

        lineNumberMapper.removeByCacheLineNumber(0);
        Assert.assertFalse(lineNumberMapper.isEmpty());
        Assert.assertFalse(lineNumberMapper.containsCacheLineNumber(0));
        Assert.assertFalse(lineNumberMapper.containsConnectionLineNumber(10));
        Assert.assertTrue(lineNumberMapper.containsCacheLineNumber(-2));
        Assert.assertTrue(lineNumberMapper.containsConnectionLineNumber(-1));
        Assert.assertEquals(-1, lineNumberMapper.toConnectionLineNumber(-2));
        Assert.assertEquals(-2, lineNumberMapper.toCacheLineNumber(-1));

        lineNumberMapper.removeByConnectionLineNumber(-1);
        Assert.assertTrue(lineNumberMapper.isEmpty());
        Assert.assertFalse(lineNumberMapper.containsCacheLineNumber(0));
        Assert.assertFalse(lineNumberMapper.containsConnectionLineNumber(10));
        Assert.assertFalse(lineNumberMapper.containsCacheLineNumber(-2));
        Assert.assertFalse(lineNumberMapper.containsConnectionLineNumber(-1));
    }

    @Test(expected = DuplicateCacheLineNumberException.class)
    public void testDuplicateCacheLine()
    {
        lineNumberMapper.addMapping(0, 0);
        lineNumberMapper.addMapping(0, 1);
    }

    @Test(expected = DuplicateConnectionLineNumberException.class)
    public void testDuplicateConnectionLine()
    {
        lineNumberMapper.addMapping(0, 0);
        lineNumberMapper.addMapping(10, 0);
    }
}
