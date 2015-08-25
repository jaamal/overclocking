package tests.stress.Commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import org.junit.Test;
import commons.utils.TimeCounter;
import helpers.TestHelpers;
import serialization.primitives.IntArraySerializer;
import tests.stress.StressTestBase;

public class IntArraySerializerTest extends StressTestBase
{
    private final IntArraySerializer intArraySerializer = new IntArraySerializer();
    
    @Test
    public void runSerializeAndDeserializeArray() throws Exception {
        final int length = 1500000;
        int[] array = TestHelpers.genIntArray(length);
        
        TimeCounter serializationCounter = TimeCounter.start();
        byte[] serializedArray;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            intArraySerializer.serialize(outputStream, array);
            serializedArray = outputStream.toByteArray();
        }
        Duration serializationDuration = serializationCounter.finish();
        
        TimeCounter deserializationCounter = TimeCounter.start();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedArray)) {
            intArraySerializer.deserialize(inputStream);
        }
        Duration deserializationDuration = deserializationCounter.finish();
        System.out.println(String.format("%s serialization requires %s ms, %s deserialization requires %s ms", 
                length, serializationDuration.toMillis(), length, deserializationDuration.toMillis()));
    }
}
