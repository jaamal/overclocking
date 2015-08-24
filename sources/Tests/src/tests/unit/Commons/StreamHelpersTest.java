package tests.unit.Commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import commons.utils.StreamHelpers;
import helpers.TestHelpers;
import tests.unit.UnitTestBase;

public class StreamHelpersTest extends UnitTestBase
{
    @Test
    public void testReadWriteInt() throws IOException {
        int[] array = TestHelpers.genIntArray(1024);
        byte[] bytes = convertToBytes(array);
        
        int[] actuals = new int[1024];
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            for (int i = 0; i < actuals.length; i++) {
                actuals[i] = StreamHelpers.readInt(inputStream);
            }
        }
        Assert.assertArrayEquals(array, actuals);
    }
    
    @Test
    public void testReadWriteLong() throws IOException {
        long[] array = TestHelpers.genLongArray(1024);
        byte[] bytes = convertToBytes(array);
        
        long[] actuals = new long[1024];
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            for (int i = 0; i < actuals.length; i++) {
                actuals[i] = StreamHelpers.readLong(inputStream);
            }
        }
        Assert.assertArrayEquals(array, actuals);
    }
    
    @Test
    public void testBytesSizeForInt() throws IOException {
        int value = TestHelpers.genInt();
        byte[] actuals = convertToBytes(new int[] { value });
        Assert.assertEquals(4, actuals.length);
    }
    
    @Test
    public void testBytesSizeForLong() throws IOException {
        long value = TestHelpers.genLong();
        byte[] actuals = convertToBytes(new long[] { value });
        Assert.assertEquals(8, actuals.length);
    }

    private byte[] convertToBytes(int[] array) throws IOException
    {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            for (int i = 0; i < array.length; i++) {
                StreamHelpers.writeInt(outputStream, array[i]);
            }
            return outputStream.toByteArray();
        }
    }
    
    private byte[] convertToBytes(long[] array) throws IOException
    {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            for (int i = 0; i < array.length; i++) {
                StreamHelpers.writeLong(outputStream, array[i]);
            }
            return outputStream.toByteArray();
        }
    }
    
}
