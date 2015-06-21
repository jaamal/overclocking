package tests.unit.CompressionService.lz77;

import static org.easymock.EasyMock.expect;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.FindingSearcher;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import data.MemoryDataFactory;

public class FindingSearcherTest extends UnitTestBase
{
    private INode mockNode;
    private FindingSearcher findingSearcher;

    @Override
    public void setUp()
    {
        super.setUp();
        this.mockNode = newMock(INode.class);
        this.findingSearcher = new FindingSearcher();
    }

    @Test
    public void testSearchForFindingNullNode()
    {
        expect(mockNode.getEdges()).andReturn(new HashMap<Character, IEdge>());

        replayAll();

        Assert.assertNull(null, this.findingSearcher.search("text", new MemoryDataFactory().createCharArray("text".toCharArray()), mockNode, 2));
    }

    @Test
    public void testSearchForFindingNotEmpty()
    {
        IEdge mockEdge = newMock(IEdge.class);
        HashMap<Character, IEdge> resultList = new HashMap<Character, IEdge>();
        resultList.put('t', mockEdge);

        expect(mockNode.getEdges()).andReturn(resultList);

        replayAll();

        Assert.assertEquals(null, this.findingSearcher.search("text", new MemoryDataFactory().createCharArray("text".toCharArray()), this.mockNode, 1));
    }

    @Test
    public void testSearchForFindingSeveralEdges()
    {
        IEdge[] mockEdges = new IEdge[]{newMock(IEdge.class), newMock(IEdge.class), newMock(IEdge.class)};
        HashMap<Character, IEdge> resultList = new HashMap<Character, IEdge>();
        resultList.put('t', mockEdges[0]);
        resultList.put('e', mockEdges[1]);
        resultList.put('x', mockEdges[2]);

        expect(mockNode.getEdges()).andReturn(resultList).anyTimes();
        expect(mockEdges[0].beginPosition()).andReturn(0).anyTimes();
        expect(mockEdges[1].beginPosition()).andReturn(1).anyTimes();

        replayAll();

        Assert.assertEquals(mockEdges[1], this.findingSearcher.search("text", new MemoryDataFactory().createCharArray("e".toCharArray()), this.mockNode, 0));
    }
}
