package tests.unit.CompressionService.lz77;

import static org.easymock.EasyMock.expect;
import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;

import compressingCore.dataAccess.IReadableCharArray;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.Finder;
import compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree.IFindingSearcher;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Location;

public class FinderTest extends UnitTestBase
{
    private IFindingSearcher mockFindingSearcher;
    private Finder finder;
    private String text;
    private IReadableCharArray string;

    @Override
    public void setUp()
    {
        super.setUp();
        this.mockFindingSearcher = newMock(IFindingSearcher.class);
    }

    @Test
    public void testEmptySubstring()
    {
        this.text = "text";
        this.string = new MemoryReadableCharArray("");
        this.finder = new Finder(this.text, this.string, this.mockFindingSearcher);
        INode mockNode = newMock(INode.class);

        replayAll();

        Location actual = this.finder.search(mockNode);
        Assert.assertEquals(Location.create(0, 0), actual);
    }

    @Test
    public void testNullEdge()
    {
        this.text = "text";
        this.string = new MemoryReadableCharArray("aaa");
        this.finder = new Finder(this.text, this.string, mockFindingSearcher);
        INode mockNode = newMock(INode.class);

        expect(mockFindingSearcher.search(this.text, this.string, mockNode, 0)).andReturn(null);

        replayAll();

        Assert.assertEquals(Location.create(0, 0), this.finder.search(mockNode));
    }

    @Test
    public void testSubstringFound()
    {
        this.text = "text";
        this.string = new MemoryReadableCharArray("tex");
        this.finder = new Finder(this.text, this.string, this.mockFindingSearcher);
        INode mockNode = newMock(INode.class);
        IEdge mockEdge = newMock(IEdge.class);

        expect(mockFindingSearcher.search(this.text, this.string, mockNode, 0)).andReturn(mockEdge);
        expect(mockEdge.beginPosition()).andReturn(0).anyTimes();
        expect(mockEdge.endPosition()).andReturn(3).anyTimes();
        expect(mockEdge.endNode()).andReturn(null).anyTimes();
        expect(mockEdge.getNumber()).andReturn(0).anyTimes();

        replayAll();

        Assert.assertEquals(Location.create(0, 3), this.finder.search(mockNode));
    }

    @Test
    public void testDifficultPathSubstringFound1()
    {
        this.text = "abcdefabcuvw";
        this.string = new MemoryReadableCharArray("bcdef");
        finder = new Finder(this.text, this.string, this.mockFindingSearcher);
        INode[] mockNodes = new INode[]{newMock(INode.class), newMock(INode.class)};
        IEdge[] mockEdges = new IEdge[]{newMock(IEdge.class), newMock(IEdge.class)};

        expect(mockFindingSearcher.search(this.text, this.string, mockNodes[0], 0)).andReturn(mockEdges[0]);
        expect(mockEdges[0].beginPosition()).andReturn(1).anyTimes();
        expect(mockEdges[0].endPosition()).andReturn(2).anyTimes();
        expect(mockEdges[0].endNode()).andReturn(mockNodes[1]).anyTimes();
        expect(mockEdges[0].getNumber()).andReturn(1).anyTimes();
        expect(mockFindingSearcher.search(this.text, this.string, mockNodes[1], 2)).andReturn(mockEdges[1]);
        expect(mockEdges[1].beginPosition()).andReturn(3).anyTimes();
        expect(mockEdges[1].endPosition()).andReturn(5).anyTimes();
        expect(mockEdges[1].endNode()).andReturn(null).anyTimes();
        expect(mockEdges[1].getNumber()).andReturn(1).anyTimes();

        replayAll();

        Assert.assertEquals(Location.create(1, 5), finder.search(mockNodes[0]));
    }

    @Test
    public void testDifficultPathSubstringFound2()
    {
        this.text = "abcdefabcuvw";
        this.string = new MemoryReadableCharArray("bcdqf");
        this.finder = new Finder(this.text, this.string, this.mockFindingSearcher);
        INode[] mockNodes = new INode[]{newMock(INode.class), newMock(INode.class), newMock(INode.class)};
        IEdge[] mockEdges = new IEdge[]{newMock(IEdge.class), newMock(IEdge.class)};

        expect(mockFindingSearcher.search(this.text, this.string, mockNodes[0], 0)).andReturn(mockEdges[0]);
        expect(mockEdges[0].beginPosition()).andReturn(1).anyTimes();
        expect(mockEdges[0].endPosition()).andReturn(2).anyTimes();
        expect(mockEdges[0].endNode()).andReturn(mockNodes[1]).anyTimes();
        expect(mockEdges[0].getNumber()).andReturn(1).anyTimes();
        expect(mockFindingSearcher.search(this.text, this.string, mockNodes[1], 2)).andReturn(mockEdges[1]);
        expect(mockEdges[1].beginPosition()).andReturn(3).anyTimes();
        expect(mockEdges[1].endPosition()).andReturn(5).anyTimes();
        expect(mockEdges[1].endNode()).andReturn(mockNodes[2]).anyTimes();
        expect(mockEdges[1].getNumber()).andReturn(1).anyTimes();

        replayAll();

        Assert.assertEquals(Location.create(1, 3), this.finder.search(mockNodes[0]));
    }
}
