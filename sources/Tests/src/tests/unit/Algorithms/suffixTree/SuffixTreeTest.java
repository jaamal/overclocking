package tests.unit.Algorithms.suffixTree;

import org.junit.Assert;
import org.junit.Test;
import suffixTree.Edge;
import suffixTree.Node;
import suffixTree.SuffixTree;
import tests.unit.UnitTestBase;

public class SuffixTreeTest extends UnitTestBase
{
    @Test
    public void testContainsInTreeWithShortLabes() {
        Node root = new Node();
           root.addEdge(Edge.create("a", new Node()))
            .to.addEdge(Edge.create("b", new Node()))
            .to.addEdge(Edge.create("r", new Node()))
            .to.addEdge(Edge.create("a", new Node()))
            .to.addEdge(Edge.create("k", new Node()))
            .to.addEdge(Edge.create("a", new Node()))
            .to.addEdge(Edge.create("d", new Node()))
            .to.addEdge(Edge.create("a", new Node()))
            .to.addEdge(Edge.create("b", new Node()))
            .to.addEdge(Edge.create("r", new Node()))
            .to.addEdge(Edge.create("a", new Node()));
        
        Assert.assertTrue(new SuffixTree(root).contains("abrakada"));
        Assert.assertTrue(new SuffixTree(root).contains("a"));
        Assert.assertFalse(new SuffixTree(root).contains("b"));
        Assert.assertFalse(new SuffixTree(root).contains("abraba"));
    }
    
    @Test
    public void testContainsInTreeWithLongLabes() {
        Node root = new Node();
           root.addEdge(Edge.create("abra", new Node()))
            .to.addEdge(Edge.create("kadab", new Node()))
            .to.addEdge(Edge.create("ra", new Node()));
        
        Assert.assertTrue(new SuffixTree(root).contains("abrakadabr"));
        Assert.assertTrue(new SuffixTree(root).contains("abra"));
        Assert.assertTrue(new SuffixTree(root).contains("abraka"));
        Assert.assertTrue(new SuffixTree(root).contains("a"));
        Assert.assertFalse(new SuffixTree(root).contains("b"));
        Assert.assertFalse(new SuffixTree(root).contains("abrab"));
        Assert.assertFalse(new SuffixTree(root).contains("abrakadar"));
        Assert.assertFalse(new SuffixTree(root).contains("abbakadar"));
    }
}
