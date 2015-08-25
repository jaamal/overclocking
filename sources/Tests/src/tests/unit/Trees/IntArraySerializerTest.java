package tests.unit.Trees;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;

import helpers.TestHelpers;
import serialization.primitives.IntArraySerializer;
import tests.unit.UnitTestBase;

public class IntArraySerializerTest extends UnitTestBase{

    @Test
    public void testSerializeAndDeserializeEmptyArray() throws Exception {
        runSerializeAndDeserializeArray(new int[0], 4);
    }
    
    @Test
    public void testSerializeAndDeserializeShortArray() throws Exception {
        runSerializeAndDeserializeArray(new int[] { 1, 2, 3 }, 11);
        runSerializeAndDeserializeArray(new int[] { 1, 2, 3, 4, 5, 6 }, 18);
    }
    
    @Test
    public void testSerializeAndDeserializeWithNegativeInts() throws Exception {
        runSerializeAndDeserializeArray(new int[] {-1, -4, 77634, -332, -32}, 31);
    }
    
    @Test
    public void testSerializeAndDeserializeOnlyPositiveInts() throws Exception {
        runSerializeAndDeserializeArray(new int[] {1, 4, 6766, 77634, 332, 32}, 22);
    }
    
    @Test
    public void testSerializeAndDeserializeRandomArray() throws Exception {
        runSerializeAndDeserializeArray(TestHelpers.genIntArray(100), -1);
    }
    
    private static void runSerializeAndDeserializeArray(int[] array, int expectedSerializedSize) throws Exception {
        IntArraySerializer intArraySerializer = new IntArraySerializer();
        byte[] serializedArray;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            intArraySerializer.serialize(outputStream, array);
            serializedArray = outputStream.toByteArray();
        }
        if (expectedSerializedSize > 0) {
            Assert.assertEquals(expectedSerializedSize, serializedArray.length);
        }
        
        int[] actuals;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedArray)) {
            actuals = intArraySerializer.deserialize(inputStream);
        }
        Assert.assertArrayEquals(array, actuals);
    }
}
