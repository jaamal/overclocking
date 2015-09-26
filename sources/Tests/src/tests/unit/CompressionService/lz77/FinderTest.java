package tests.unit.CompressionService.lz77;

import static org.easymock.EasyMock.expect;
import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.Finder;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.IFindingSearcher;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.MemoryDataFactory;
import data.charArray.IReadableCharArray;

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
        this.string = new MemoryDataFactory().createCharArray("".toCharArray());
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
        this.string = new MemoryDataFactory().createCharArray("aaa".toCharArray());
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
        this.string = new MemoryDataFactory().createCharArray("tex".toCharArray());
        this.finder = new Finder(this.text, this.string, this.mockFindingSearcher);
        INode mockNode = newMock(INode.class);
        IEdge mockEdge = newMock(IEdge.class);

        expect(mockFindingSearcher.search(this.text, this.string, mockNode, 0)).andReturn(mockEdge);
        expect(mockEdge.fromPosition()).andReturn(0).anyTimes();
        expect(mockEdge.toPosition()).andReturn(3).anyTimes();
        expect(mockEdge.toNode()).andReturn(null).anyTimes();
        expect(mockEdge.getNumber()).andReturn(0).anyTimes();

        replayAll();

        Assert.assertEquals(Location.create(0, 3), this.finder.search(mockNode));
    }

    @Test
    public void testDifficultPathSubstringFound1()
    {
        this.text = "abcdefabcuvw";
        this.string = new MemoryDataFactory().createCharArray("bcdef".toCharArray());
        finder = new Finder(this.text, this.string, this.mockFindingSearcher);
        INode[] mockNodes = new INode[]{newMock(INode.class), newMock(INode.class)};
        IEdge[] mockEdges = new IEdge[]{newMock(IEdge.class), newMock(IEdge.class)};

        expect(mockFindingSearcher.search(this.text, this.string, mockNodes[0], 0)).andReturn(mockEdges[0]);
        expect(mockEdges[0].fromPosition()).andReturn(1).anyTimes();
        expect(mockEdges[0].toPosition()).andReturn(2).anyTimes();
        expect(mockEdges[0].toNode()).andReturn(mockNodes[1]).anyTimes();
        expect(mockEdges[0].getNumber()).andReturn(1).anyTimes();
        expect(mockFindingSearcher.search(this.text, this.string, mockNodes[1], 2)).andReturn(mockEdges[1]);
        expect(mockEdges[1].fromPosition()).andReturn(3).anyTimes();
        expect(mockEdges[1].toPosition()).andReturn(5).anyTimes();
        expect(mockEdges[1].toNode()).andReturn(null).anyTimes();
        expect(mockEdges[1].getNumber()).andReturn(1).anyTimes();

        replayAll();

        Assert.assertEquals(Location.create(1, 5), finder.search(mockNodes[0]));
    }

    @Test
    public void testDifficultPathSubstringFound2()
    {
        this.text = "abcdefabcuvw";
        this.string = new MemoryDataFactory().createCharArray("bcdqf".toCharArray());
        this.finder = new Finder(this.text, this.string, this.mockFindingSearcher);
        INode[] mockNodes = new INode[]{newMock(INode.class), newMock(INode.class), newMock(INode.class)};
        IEdge[] mockEdges = new IEdge[]{newMock(IEdge.class), newMock(IEdge.class)};

        expect(mockFindingSearcher.search(this.text, this.string, mockNodes[0], 0)).andReturn(mockEdges[0]);
        expect(mockEdges[0].fromPosition()).andReturn(1).anyTimes();
        expect(mockEdges[0].toPosition()).andReturn(2).anyTimes();
        expect(mockEdges[0].toNode()).andReturn(mockNodes[1]).anyTimes();
        expect(mockEdges[0].getNumber()).andReturn(1).anyTimes();
        expect(mockFindingSearcher.search(this.text, this.string, mockNodes[1], 2)).andReturn(mockEdges[1]);
        expect(mockEdges[1].fromPosition()).andReturn(3).anyTimes();
        expect(mockEdges[1].toPosition()).andReturn(5).anyTimes();
        expect(mockEdges[1].toNode()).andReturn(mockNodes[2]).anyTimes();
        expect(mockEdges[1].getNumber()).andReturn(1).anyTimes();

        replayAll();

        Assert.assertEquals(Location.create(1, 3), this.finder.search(mockNodes[0]));
    }
}
