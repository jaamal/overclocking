package tests.unit.Storage;

import org.junit.Assert;
import org.junit.Test;

import storage.ColumnFormatter;
import tests.unit.UnitTestBase;

public class ColumnFormatterTest extends UnitTestBase {
    @Test
    public void testTotalInt() {
        Assert.assertEquals("0000000000", ColumnFormatter.asPositiveInt(0));
        Assert.assertEquals("0000000001", ColumnFormatter.asPositiveInt(1));
        Assert.assertEquals("0687878978", ColumnFormatter.asPositiveInt(687878978));
        Assert.assertEquals("2147483647", ColumnFormatter.asPositiveInt(Integer.MAX_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInt() {
        ColumnFormatter.asPositiveInt(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLong() {
        ColumnFormatter.asPositiveInt(-1);
    }
}
