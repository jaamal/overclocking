package tests.unit.Trees.nodeProviders.indexSets;

import java.util.HashSet;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import tree.nodeProviders.indexSets.ConcurrentMemoryIndexSet;

public class ConcurrentMemoryIndexSetTest extends UnitTestBase {

    private ConcurrentMemoryIndexSet indexSet;

    @Override
    public void setUp() {
        super.setUp();
        indexSet = new ConcurrentMemoryIndexSet();
    }

    @Test()
    public void testPopEmpty() {
        Assert.assertEquals(-1, indexSet.tryPopAny());
    }

    @Test
    public void testAddOne() {
        Assert.assertEquals(-1, indexSet.tryPopAny());
        indexSet.add(10);
        Assert.assertEquals(10, indexSet.tryPopAny());
        Assert.assertEquals(-1, indexSet.tryPopAny());
    }

    @Test
    public void testStress() {
        HashSet<Long> set = new HashSet<Long>();
        Random random = new Random();
        for (int i = 0; i < 100000; ++i) {
            if (set.size() == 0)
                Assert.assertEquals(-1, indexSet.tryPopAny());
            if (set.size() == 10000 || random.nextInt(4) == 0) {
                long poppedIndex = indexSet.tryPopAny();
                if (set.size() == 0) {
                    Assert.assertEquals(-1, poppedIndex);
                } else {
                    Assert.assertTrue(set.contains(poppedIndex));
                    set.remove(poppedIndex);
                }
            } else {
                long index;
                do {
                    index = random.nextLong();
                } while (set.contains(index));
                set.add(index);
                indexSet.add(index);
            }
        }
    }
}
