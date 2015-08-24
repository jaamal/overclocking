package tests.unit.Commons;

import org.junit.Test;

import commons.utils.NumericUtils;
import junit.framework.Assert;
import tests.unit.UnitTestBase;

public class NumericUtilsTest extends UnitTestBase
{
    @Test
    public void testConvertingCorrectnessForLong()
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
    public void testIntFromFloatingBytes(){
        doTestIntFromFloatingBytes(new byte[] {1}, 1);
        doTestIntFromFloatingBytes(new byte[] {2, 1}, 258);
        doTestIntFromFloatingBytes(new byte[] {3, 2, 1}, 66051);
    }
    
    @Test
    public void testToBytesSize() {
        Assert.assertEquals(1, NumericUtils.toFloatingBytes(1).length);
        Assert.assertEquals(2, NumericUtils.toFloatingBytes(257).length);
        Assert.assertEquals(4, NumericUtils.toFloatingBytes(Integer.MAX_VALUE).length);
    }
    
    private static void doTestIntFromFloatingBytes(byte[] buffer, int expected) {
        Assert.assertEquals(expected, NumericUtils.intFromFloatingBytes(buffer));
    }
    
    private static void doTestLong(long value)
    {
        byte[] bytes = new byte[9];
        NumericUtils.longToBytes(value, bytes, 1);
        Assert.assertEquals(value, NumericUtils.bytesToLong(bytes, 1));
    }
}
