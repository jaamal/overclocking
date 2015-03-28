package tests.unit.Algorithms.patternMatching.localsearch;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.localsearch.LocalSearchResult;
import tests.unit.UnitTestBase;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class LocalSearchResultTest extends UnitTestBase {

    private LocalSearchResult localSearchResult;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        localSearchResult = new LocalSearchResult();
    }

    private void add(ArithmeticProgression... aps) {
        for (ArithmeticProgression ap : aps)
            localSearchResult.add(ap);
    }

    private void checkContains(ArithmeticProgression... aps) {
        Queue<ArithmeticProgression> queue = new ArrayDeque<>(Arrays.asList(aps));
        for(int i = 0; i < localSearchResult.size(); ++i)
            Assert.assertEquals(localSearchResult.get(i), queue.remove());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testEmptyWhenNoAPAdded() {
        checkContains();
    }

    @Test
    public void testEmptyWhenEmptyAPAdded() {
        add(ArithmeticProgression.Empty);
        checkContains();
    }

    @Test
    public void testTwoOneElementProgressionWithOneElementDistance() {
        add(ArithmeticProgression.create(0, 1, 1), ArithmeticProgression.create(1, 1, 1));
        checkContains(ArithmeticProgression.create(0, 1, 2));
    }


    @Test
    public void testTwoOneElementProgressionWithNoDistance() {
        add(ArithmeticProgression.create(0, 1, 1), ArithmeticProgression.create(0, 1, 1));
        checkContains(ArithmeticProgression.create(0, 1, 1));
    }


    @Test
    public void testTwoOneElementProgressionWithMoreThanOneElementDistance() {
        add(ArithmeticProgression.create(0, 1, 1), ArithmeticProgression.create(2, 1, 1));
        checkContains(ArithmeticProgression.create(0, 2, 2));
    }

    @Test(expected = IllegalStateException.class)
    public void testWhenMoreThanTwoResultProgression() throws IllegalStateException {
        add(ArithmeticProgression.create(0, 1, 2), ArithmeticProgression.create(3, 1, 1), ArithmeticProgression.create(7, 1, 6));
    }
}
