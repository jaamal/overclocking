package tests.unit.Algorithms.patternMatching.arithmeticProgression.table;

import junit.framework.Assert;

import org.junit.Test;

import patternMatching.fcpm.table.builder.IntervalTextIterator;
import tests.unit.UnitTestBase;

public class IntervalTextIteratorTests extends UnitTestBase{

    @Test
    public void testWhenEmptyInterval() {
        Assert.assertFalse(new IntervalTextIterator(1, 1, 1).hasNext());
        Assert.assertFalse(new IntervalTextIterator(1, 1, 1).hasNext());
    }

    @Test
    public void testWhenBatchSizeMoreThenTotal() {
        IntervalTextIterator iterator = new IntervalTextIterator(0, 2, 3);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testWhenFullInterval() {
        IntervalTextIterator iterator = new IntervalTextIterator(0, 2, 2);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}

