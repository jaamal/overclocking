package tests.unit.Commons;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;

import java.util.regex.Pattern;

public class RegexTest extends UnitTestBase
{
    @Test
    public void test()
    {
        Pattern p = Pattern.compile("random-.*\\.txt");
        Assert.assertTrue(p.matcher("random-49.txt").matches());
        Assert.assertTrue(p.matcher("random-.txt").matches());
        Assert.assertFalse(p.matcher("random-txt").matches());
        Assert.assertFalse(p.matcher("random49.txt").matches());
        Assert.assertFalse(p.matcher("random-49.gz").matches());
        Assert.assertFalse(p.matcher("aaaa.gz").matches());
    }
}
