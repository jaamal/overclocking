package tests.unit.Algorithms.algorithmsForComparison;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import patternMatching.kmp.KMPMatcher;
import tests.unit.UnitTestBase;

public class KMPAlgoTest extends UnitTestBase {

    @Test
    public void testContainsOneLetterPattern() {
        KMPMatcher kmp = KMPMatcher.newMatcher("abacaba", "a");

        assertTrue(kmp.contains());
    }

    @Test
    public void testContainsTrue() {
        KMPMatcher kmp = KMPMatcher.newMatcher("abacaba", "aca");

        assertTrue(kmp.contains());
    }

    @Test
    public void testContainsFalse() {
        KMPMatcher kmp = KMPMatcher.newMatcher("abacaba", "abad");

        assertFalse(kmp.contains());
    }


    //TODO this test fails
    /*@Test
    public void testCount() {
        KMPMatcher kmpMatcher = KMPMatcher.newMatcher("abrakadabra", "a");
        assertEquals(kmpMatcher.count(), 5);
        kmpMatcher = KMPMatcher.newMatcher("abrakadabra", "b");
        assertEquals(kmpMatcher.count(), 2);
        kmpMatcher = KMPMatcher.newMatcher("abrakadabra", "r");
        assertEquals(kmpMatcher.count(), 2);
        kmpMatcher = KMPMatcher.newMatcher("abrakadabra", "k");
        assertEquals(kmpMatcher.count(), 1);
        kmpMatcher = KMPMatcher.newMatcher("abrakadabra", "d");
        assertEquals(kmpMatcher.count(), 1);
    }*/

}
