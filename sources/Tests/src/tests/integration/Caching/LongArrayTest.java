package tests.integration.Caching;

import org.junit.Assert;
import org.junit.Test;

import caching.MemoryStorage;
import compressingCore.dataAccess.LongArray;
import tests.integration.IntegrationTestBase;

public class LongArrayTest extends IntegrationTestBase
{
    @Test(expected=IndexOutOfBoundsException.class)
    public void testDefaults() {
        try (LongArray longArray = new LongArray(new MemoryStorage<Long>(), 3)){
            longArray.get(0);
        }
    }
    
    @Test
    public void testGetAndSet() {
        try (LongArray longArray = new LongArray(new MemoryStorage<Long>(), 3)){
            longArray.set(0, 16);
            Assert.assertEquals(16L, longArray.get(0));
            longArray.set(1, Integer.MAX_VALUE);
            Assert.assertEquals((long)Integer.MAX_VALUE, longArray.get(1));
            longArray.set(2, Integer.MAX_VALUE + 100);
            Assert.assertEquals(Integer.MAX_VALUE + 100, longArray.get(2));
        }
    }
    
    @Test
    public void testMultiSet() {
        try (LongArray longArray = new LongArray(new MemoryStorage<Long>(), 3)){
            long[] array = new long[] {16L, Integer.MAX_VALUE, (long) Integer.MAX_VALUE + 100 };
            longArray.set(0, array);
            Assert.assertEquals(16L, longArray.get(0));
            Assert.assertEquals((long) Integer.MAX_VALUE, longArray.get(1));
            Assert.assertEquals((long) Integer.MAX_VALUE + 100, longArray.get(2));
        }
    }
}
