package tests.unit.Algorithms.suffixTree;

import org.junit.Assert;
import org.junit.Test;
import helpers.StringHelpers;
import helpers.TestHelpers;
import suffixTree.ISuffixTree;
import suffixTree.SuffixTreeBuilder;
import tests.unit.UnitTestBase;

public class SuffixTreeBuilderTest extends UnitTestBase
{
    @Test
    public void testBuildForAbrakadabra() {
        SuffixTreeBuilder builder = new SuffixTreeBuilder();
        builder.append("abrakadabra");
        ISuffixTree actual = builder.toSuffixTree();
        
        String[] substrings = StringHelpers.getOwnSubstrings("abrakadabra");
        for (String substring : substrings) {
            Assert.assertTrue(String.format("contains %s substring", substring), actual.contains(substring));
        }
    }
    
    @Test
    public void testBuildForBigString() {
        String text = TestHelpers.genString(100);
        SuffixTreeBuilder builder = new SuffixTreeBuilder();
        builder.append(text);
        ISuffixTree actual = builder.toSuffixTree();
        
        String[] substrings = StringHelpers.getOwnSubstrings(text);
        for (String substring : substrings) {
            Assert.assertTrue(String.format("contains %s substring", substring), actual.contains(substring));
        }
    }
}
