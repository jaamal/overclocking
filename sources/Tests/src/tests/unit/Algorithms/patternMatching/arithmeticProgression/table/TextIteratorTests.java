package tests.unit.Algorithms.patternMatching.arithmeticProgression.table;

import junit.framework.Assert;

import org.junit.Test;

import patternMatching.fcpm.table.builder.OrderedTextIterator;
import tests.unit.UnitTestBase;

public class TextIteratorTests extends UnitTestBase {

    @Test
    public void testWhenEmptyInterval() {
        Assert.assertFalse(new OrderedTextIterator(1, 1, 1).hasNext());
    }

    @Test
    public void testWhenBatchSizeMoreThenTotal() {
        OrderedTextIterator iterator = new OrderedTextIterator(0, 4, 2);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testWhenFullInterval() {
        OrderedTextIterator iterator = new OrderedTextIterator(0, 4, 2);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}
