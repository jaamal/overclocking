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
        byte[] bytes = prepareByteArray(array);
        
        int[] actuals = new int[1024];
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            for (int i = 0; i < actuals.length; i++) {
                actuals[i] = StreamHelpers.readInt(inputStream);
            }
        }
        Assert.assertArrayEquals(array, actuals);
    }

    private byte[] prepareByteArray(int[] array) throws IOException
    {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            for (int i = 0; i < array.length; i++) {
                StreamHelpers.writeInt(outputStream, array[i]);
            }
            return outputStream.toByteArray();
        }
    }
    
}
