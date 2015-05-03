package tests.unit.Commons;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;

public class StringBuilderTest extends UnitTestBase
{
    @Test
    public void TestMultipleToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("a");
        builder.append("b");
        builder.append("c");
        
        Assert.assertEquals("abc", builder.toString());
        Assert.assertEquals("abc", builder.toString());
    }
}
