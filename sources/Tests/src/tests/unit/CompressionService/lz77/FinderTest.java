package tests.unit.CompressionService.lz77;

import static org.easymock.EasyMock.expect;
import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.Finder;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.MemoryDataFactory;
import data.charArray.IReadableCharArray;

public class FinderTest extends UnitTestBase
{
    private Finder finder;
    private IReadableCharArray pattern;

    @Override
    public void setUp()
    {
        super.setUp();

        this.finder = new Finder();
    }

    @Test
    public void testEmptyPattern()
    {
        pattern = new MemoryDataFactory().createCharArray("".toCharArray());
        INode mockNode = newMock(INode.class);

        replayAll();

        Location actual = this.finder.search(mockNode, "text", pattern);
        Assert.assertEquals(Location.create(0, 0), actual);
    }
    
    @Test
    public void testEmptyRoot()
    {
        pattern = new MemoryDataFactory().createCharArray("".toCharArray());

        Location actual = this.finder.search(null, "text", pattern);
        Assert.assertEquals(Location.create(0, 0), actual);
    }

    @Test
    public void testNullEdge()
    {
        this.pattern = new MemoryDataFactory().createCharArray("aaa".toCharArray());
        INode mockNode = newMock(INode.class);

        expect(mockNode.findEdge('a')).andReturn(null);
        replayAll();

        Assert.assertEquals(Location.create(0, 0), this.finder.search(mockNode, "text", pattern));
    }

    @Test
    public void testPatternFound()
    {
        this.pattern = new MemoryDataFactory().createCharArray("tex".toCharArray());
        INode mockNode = newMock(INode.class);
        IEdge mockEdge = newMock(IEdge.class);

        expect(mockNode.findEdge('t')).andReturn(mockEdge);
        expect(mockEdge.fromPosition()).andReturn(0).anyTimes();
        expect(mockEdge.toPosition()).andReturn(3).anyTimes();
        expect(mockEdge.toNode()).andReturn(null).anyTimes();
        expect(mockEdge.getNumber()).andReturn(0).anyTimes();

        replayAll();

        Assert.assertEquals(Location.create(0, 3), this.finder.search(mockNode, "text", pattern));
    }

    @Test
    public void testDifficultPathPatternFound1()
    {
        this.pattern = new MemoryDataFactory().createCharArray("bcdef".toCharArray());
        INode[] mockNodes = new INode[]{newMock(INode.class), newMock(INode.class)};
        IEdge[] mockEdges = new IEdge[]{newMock(IEdge.class), newMock(IEdge.class)};

        expect(mockNodes[0].findEdge('b')).andReturn(mockEdges[0]);
        expect(mockEdges[0].fromPosition()).andReturn(1).anyTimes();
        expect(mockEdges[0].toPosition()).andReturn(2).anyTimes();
        expect(mockEdges[0].toNode()).andReturn(mockNodes[1]).anyTimes();
        expect(mockEdges[0].getNumber()).andReturn(1).anyTimes();
        expect(mockNodes[1].findEdge('d')).andReturn(mockEdges[1]);
        expect(mockEdges[1].fromPosition()).andReturn(3).anyTimes();
        expect(mockEdges[1].toPosition()).andReturn(5).anyTimes();
        expect(mockEdges[1].toNode()).andReturn(null).anyTimes();
        expect(mockEdges[1].getNumber()).andReturn(1).anyTimes();

        replayAll();

        Assert.assertEquals(Location.create(1, 5), finder.search(mockNodes[0], "abcdefabcuvw", pattern));
    }

    @Test
    public void testDifficultPathSubstringFound2()
    {
        this.pattern = new MemoryDataFactory().createCharArray("bcdqf".toCharArray());
        INode[] mockNodes = new INode[]{newMock(INode.class), newMock(INode.class), newMock(INode.class)};
        IEdge[] mockEdges = new IEdge[]{newMock(IEdge.class), newMock(IEdge.class)};

        expect(mockNodes[0].findEdge('b')).andReturn(mockEdges[0]);
        expect(mockEdges[0].fromPosition()).andReturn(1).anyTimes();
        expect(mockEdges[0].toPosition()).andReturn(2).anyTimes();
        expect(mockEdges[0].toNode()).andReturn(mockNodes[1]).anyTimes();
        expect(mockEdges[0].getNumber()).andReturn(1).anyTimes();
        expect(mockNodes[1].findEdge('d')).andReturn(mockEdges[1]);
        expect(mockEdges[1].fromPosition()).andReturn(3).anyTimes();
        expect(mockEdges[1].toPosition()).andReturn(5).anyTimes();
        expect(mockEdges[1].toNode()).andReturn(mockNodes[2]).anyTimes();
        expect(mockEdges[1].getNumber()).andReturn(1).anyTimes();

        replayAll();

        Assert.assertEquals(Location.create(1, 3), this.finder.search(mockNodes[0], "abcdefabcuvw", pattern));
    }
}
