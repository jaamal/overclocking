package tests.unit.Commons;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import commons.utils.NumericUtils;

public class NumericUtilsTest extends UnitTestBase
{
    @Test
    public void testConvertingLong()
    {
        doTestLong(Long.MIN_VALUE);
        doTestLong(Long.MAX_VALUE);
        doTestLong(0L);
        doTestLong(-23897238278L);
        doTestLong(23897238278L);
        doTestLong(9348293829382L);
        doTestLong(1L);
        doTestLong(-1L);
        doTestLong(23L);
    }

    @Test
    public void testConvertingInt()
    {
        doTestInt(Integer.MAX_VALUE);
        doTestInt(Integer.MIN_VALUE);
        doTestInt(782378);
        doTestInt(-2782738);
        doTestInt(1);
        doTestInt(-1);
        doTestInt(0);
        doTestInt(429834929);
        doTestInt(-429834929);
    }

    private static void doTestInt(int value)
    {
        byte[] bytes = new byte[6];
        NumericUtils.intToBytes(value, bytes, 2);
        Assert.assertEquals(value, NumericUtils.bytesToInt(bytes, 2));
    }

    private static void doTestLong(long value)
    {
        byte[] bytes = new byte[9];
        NumericUtils.longToBytes(value, bytes, 1);
        Assert.assertEquals(value, NumericUtils.bytesToLong(bytes, 1));
    }
}
