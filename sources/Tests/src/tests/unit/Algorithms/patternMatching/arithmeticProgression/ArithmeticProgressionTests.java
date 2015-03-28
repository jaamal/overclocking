package tests.unit.Algorithms.patternMatching.arithmeticProgression;

import org.junit.Test;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import tests.unit.UnitTestBase;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertTrue;
import static patternMatching.fcpm.arithmeticProgression.ArithmeticProgression.Empty;
import static patternMatching.fcpm.arithmeticProgression.ArithmeticProgression.create;

public class ArithmeticProgressionTests extends UnitTestBase {

    @Test
    public void testMerge() {
        assertEquals(create(1, 2, 4), create(1, 2, 4).merge(Empty));
        assertEquals(create(1, 2, 4), Empty.merge(create(1, 2, 4)));
        assertEquals(create(1, 2, 4), create(1, 2, 3).merge(create(7, 1, 1)));
        assertEquals(create(1, 2, 5), create(1, 2, 3).merge(create(7, 2, 2)));

        assertEquals(create(0, 1, 1), create(0, 1, 1).merge(create(0, 1, 1)));

        assertEquals(create(1, 2, 2), create(1, 1, 1).merge(create(3, 1, 1)));
        assertMergeFailed(create(1, 3, 2), create(2, 3, 2));
        assertMergeFailed(create(1, 3, 2), create(5, 1, 1));
        assertMergeFailed(create(5, 1, 1), create(1, 3, 2));
    }

    private static void assertMergeFailed(ArithmeticProgression first, ArithmeticProgression second) {
        try {
            first.merge(second);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testShift() {
        assertEquals(Empty, Empty.shift(1));
        assertEquals(create(3, 2, 3), create(1, 2, 3).shift(2));
    }

    @Test
    public void testElementsCount() {
        assertEquals(0, Empty.elementsCount);
        assertEquals(3, create(1, 2, 3).elementsCount);
    }


    @Test
    public void testLength() {
        assertEquals(0, Empty.length());
        assertEquals(5, create(1, 2, 3).length());
    }

    @Test
    public void testElement() {
        assertEquals(1, create(1, 2, 3).elementAt(0));
        assertEquals(3, create(1, 2, 3).elementAt(1));
        assertEquals(5, create(1, 2, 3).elementAt(2));
    }

    @Test
    public void testTruncate() {
        assertEquals(create(1, 2, 2), create(1, 2, 2).truncate(1, 3));
        assertEquals(create(1, 2, 2), create(1, 2, 2).truncate(1, 5));
        assertEquals(create(1, 2, 2), create(1, 2, 2).truncate(0, 3));
        assertEquals(create(3, 1, 1), create(1, 2, 2).truncate(3, 10));
        assertEquals(create(1, 1, 1), create(1, 2, 2).truncate(0, 1));
        assertEquals(create(1, 1, 1), create(1, 2, 2).truncate(0, 2));
        assertEquals(create(3, 1, 1), create(1, 2, 2).truncate(2, 3));
        assertEquals(create(3, 1, 1), create(1, 2, 2).truncate(2, 4));

        assertEquals(create(8, 1, 1), create(8, 3, 4).truncate(0, 8));
        assertEquals(create(8, 3, 2), create(8, 3, 4).truncate(0, 12));
        assertEquals(create(8, 3, 4), create(8, 3, 4).truncate(0, 100));
        assertEquals(create(11, 3, 2), create(8, 3, 4).truncate(11, 14));
        assertEquals(create(17, 1, 1), create(8, 3, 4).truncate(17, 17));
        assertEquals(create(11, 1, 1), create(8, 3, 4).truncate(11, 11));
        assertEquals(Empty, create(7, 1, 1).truncate(6, 6));
        assertEquals(Empty, create(5, 1, 1).truncate(6, 6));
    }


    @Test
    public void testContains() {
        assertFalse(Empty.contains(1));
        assertFalse(ArithmeticProgression.create(1).contains(0));
        assertFalse(ArithmeticProgression.create(1).contains(2));
        assertTrue(ArithmeticProgression.create(1).contains(1));

        assertFalse(create(1, 2, 3).contains(0));
        assertFalse(create(1, 2, 3).contains(6));
        assertFalse(create(1, 2, 3).contains(2));
        assertFalse(create(1, 2, 3).contains(4));
        assertTrue(create(1, 2, 3).contains(1));
        assertTrue(create(1, 2, 3).contains(3));
        assertTrue(create(1, 2, 3).contains(5));


    }
}
