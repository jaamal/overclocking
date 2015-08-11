package tests.unit.Commons;

import org.junit.Test;

import commons.utils.NumericUtils;
import junit.framework.Assert;
import tests.unit.UnitTestBase;

public class NumericUtilsTest extends UnitTestBase
{
    private static int[] testIntArray = new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE, 782378, -2782738, 1, -1, 0, 429834929, -429834929 };
    
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
    public void testConvertingCorrectnessForInt()
    {
        for (int i = 0; i < testIntArray.length; i++) {
            doTestInt(testIntArray[i]);
        }
    }
    
    @Test
    public void testConvertingCorrectnessForIntWithShift()
    {
        for (int i = 0; i < testIntArray.length; i++) {
            doTestIntWithShift(testIntArray[i]);
        }
    }
    
    @Test
    public void testIntFromFloatingBytes(){
        doTestIntFromFloatingBytes(new byte[] {1}, 1);
        doTestIntFromFloatingBytes(new byte[] {2, 1}, 258);
        doTestIntFromFloatingBytes(new byte[] {3, 2, 1}, 66051);
    }
    
    @Test
    public void testToBytesSize() {
        Assert.assertEquals(4, NumericUtils.toBytes(1).length);
        Assert.assertEquals(4, NumericUtils.toBytes(Integer.MAX_VALUE).length);
        Assert.assertEquals(4, NumericUtils.toBytes(257).length);
        Assert.assertEquals(1, NumericUtils.toFloatingBytes(1).length);
        Assert.assertEquals(2, NumericUtils.toFloatingBytes(257).length);
        Assert.assertEquals(4, NumericUtils.toFloatingBytes(Integer.MAX_VALUE).length);
    }
    
    private static void doTestIntFromFloatingBytes(byte[] buffer, int expected) {
        Assert.assertEquals(expected, NumericUtils.intFromFloatingBytes(buffer));
    }
    
    private static void doTestInt(int value)
    {
        byte[] bytes = NumericUtils.toBytes(value);
        Assert.assertEquals(value, NumericUtils.intFromBytes(bytes));
    }

    private static void doTestIntWithShift(int value) {
        byte[] bytes = NumericUtils.toBytes(value);
        byte[] shiftedBytes = new byte[bytes.length + 10];
        for (int i = 0; i < bytes.length; i++) 
            shiftedBytes[i + 10] = bytes[i];
        
        Assert.assertEquals(value, NumericUtils.intFromBytes(shiftedBytes, 10, 4));
    }
    
    private static void doTestLong(long value)
    {
        byte[] bytes = new byte[9];
        NumericUtils.longToBytes(value, bytes, 1);
        Assert.assertEquals(value, NumericUtils.bytesToLong(bytes, 1));
    }
}
