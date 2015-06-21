package tests.unit.CompressingCore.helpers;

import org.junit.Test;

import data.filters.automatas.StringHelpers;
import tests.unit.UnitTestBase;
import static org.junit.Assert.assertArrayEquals;

public class StringHelpersTest extends UnitTestBase
{
    @Test
    public void testZAlgorithmForEmptyString()
    {
        int[] actual = StringHelpers.applyZBlocksAlgorithm("");
        assertArrayEquals(new int[0], actual);
    }

    @Test
    public void testZAlgorithmForSingleCharString()
    {
        int[] actual = StringHelpers.applyZBlocksAlgorithm("a");
        assertArrayEquals(new int[]{0}, actual);
    }

    @Test
    public void testZAlgorithmForPowerOfCharString()
    {
        int[] actual = StringHelpers.applyZBlocksAlgorithm("aaaaa");
        assertArrayEquals(new int[]{0, 4, 3, 2, 1}, actual);
    }

    @Test
    public void testZAlgorithmUsesPreimages()
    {
        int[] actual = StringHelpers.applyZBlocksAlgorithm("ababaxababa");
        assertArrayEquals(new int[]{0, 0, 3, 0, 1, 0, 5, 0, 3, 0, 1}, actual);
    }

    @Test
    public void testZAlgorithmIntegration()
    {
        int[] actual = StringHelpers.applyZBlocksAlgorithm("aabaabcaxaabaabcy");
        assertArrayEquals(new int[]{0, 1, 0, 3, 1, 0, 0, 1, 0, 7, 1, 0, 3, 1, 0, 0, 0}, actual);
    }
}
