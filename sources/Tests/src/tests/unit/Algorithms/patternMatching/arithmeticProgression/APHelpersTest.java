package tests.unit.Algorithms.patternMatching.arithmeticProgression;

import junit.framework.TestCase;
import org.junit.Test;
import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

import static patternMatching.fcpm.arithmeticProgression.APHelpers.intersect;
import static patternMatching.fcpm.arithmeticProgression.ArithmeticProgression.Empty;


public class APHelpersTest extends TestCase {
    @Test
    public void testIntersect() {
        assertEquals(intersect(Empty, Empty), Empty);
        assertEquals(intersect(Empty, ArithmeticProgression.create(0, 1, 2)), ArithmeticProgression.Empty);
        assertEquals(intersect(ArithmeticProgression.create(1, 3, 7), ArithmeticProgression.create(0, 2, 7)), ArithmeticProgression.create(4, 6, 2));
        assertEquals(intersect(ArithmeticProgression.create(2, 2, 4), ArithmeticProgression.create(14, 2, 3)), Empty);
        assertEquals(intersect(ArithmeticProgression.create(0, 2, 3), ArithmeticProgression.create(6, 2, 3)), Empty);
        assertEquals(intersect(ArithmeticProgression.create(0, 2, 10), ArithmeticProgression.create(2, 4, 2)), ArithmeticProgression.create(2, 4, 2));

    }
}
