package tests.stress.Commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import org.junit.Test;
import commons.utils.StreamHelpers;
import commons.utils.TimeCounter;
import helpers.TestHelpers;
import tests.stress.StressTestBase;

public class StreamHelpersTest extends StressTestBase
{
    private final static int length = 1000000;
    
    @Test
    public void readWriteIntTest() throws IOException {
        int[] array = TestHelpers.genIntArray(length);
        byte[] bytes;
        
        TimeCounter writeCounter = TimeCounter.start();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            for (int i = 0; i < array.length; i++) {
                StreamHelpers.writeInt(outputStream, array[i]);
            }
            bytes = outputStream.toByteArray();
        }
        Duration writeDuration = writeCounter.finish();
        
        int[] actuals = new int[length];
        TimeCounter readCounter = TimeCounter.start();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            for (int i = 0; i < actuals.length; i++) {
                actuals[i] = StreamHelpers.readInt(inputStream);
            }
        }
        Duration readDuration = readCounter.finish();
        System.out.println(String.format("%s writes requires %s ms (average %s ms per write), %s requires %s ms (average %s ms per write)", 
                length, writeDuration.toMillis(), writeDuration.toMillis() / length,
                length, readDuration.toMillis(), readDuration.toMillis() / length));
    }
    
    @Test
    public void readWriteLongTest() throws IOException {
        long[] array = TestHelpers.genLongArray(length);
        byte[] bytes;
        
        TimeCounter writeCounter = TimeCounter.start();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            for (int i = 0; i < array.length; i++) {
                StreamHelpers.writeLong(outputStream, array[i]);
            }
            bytes = outputStream.toByteArray();
        }
        Duration writeDuration = writeCounter.finish();
        
        long[] actuals = new long[length];
        TimeCounter readCounter = TimeCounter.start();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            for (int i = 0; i < actuals.length; i++) {
                actuals[i] = StreamHelpers.readLong(inputStream);
            }
        }
        Duration readDuration = readCounter.finish();
        System.out.println(String.format("%s writes requires %s ms (average %s ms per write), %s requires %s ms (average %s ms per write)", 
                length, writeDuration.toMillis(), writeDuration.toMillis() / length,
                length, readDuration.toMillis(), readDuration.toMillis() / length));
    }
}
