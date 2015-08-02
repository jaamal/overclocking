package tests.unit.Commons;

import org.junit.Test;

import commons.utils.NumericUtils;
import junit.framework.Assert;
import tests.unit.UnitTestBase;

public class NumericUtilsTest extends UnitTestBase
{
    private static int[] testIntArray = new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE, 782378, -2782738, 1, -1, 0, 429834929, -429834929 };
    
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
        for (int i = 0; i < testIntArray.length; i++) {
            doTestInt(testIntArray[i]);
        }
    }
    
    @Test
    public void testConvertingIntWithShift()
    {
        for (int i = 0; i < testIntArray.length; i++) {
            doTestIntWithShift(testIntArray[i]);
        }
    }
    
    private static void doTestInt(int value)
    {
        byte[] bytes = NumericUtils.toBytes(value);
        Assert.assertEquals(value, NumericUtils.fromBytes(bytes));
    }

    private static void doTestIntWithShift(int value) {
        byte[] bytes = NumericUtils.toBytes(value);
        byte[] shiftedBytes = new byte[bytes.length + 10];
        for (int i = 0; i < bytes.length; i++) 
            shiftedBytes[i + 10] = bytes[i];
        
        Assert.assertEquals(value, NumericUtils.fromBytes(shiftedBytes, 10));
    }
    
    private static void doTestLong(long value)
    {
        byte[] bytes = new byte[9];
        NumericUtils.longToBytes(value, bytes, 1);
        Assert.assertEquals(value, NumericUtils.bytesToLong(bytes, 1));
    }
}
