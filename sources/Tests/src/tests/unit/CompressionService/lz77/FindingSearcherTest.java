package tests.unit.CompressionService.lz77;

import static org.easymock.EasyMock.expect;
import org.junit.Test;
import compressionservice.algorithms.lz77.suffixTree.searchingInTree.FindingSearcher;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import data.charArray.IReadableCharArray;
import junit.framework.Assert;
import tests.unit.UnitTestBase;

public class FindingSearcherTest extends UnitTestBase
{
    private INode mockNode;
    private FindingSearcher findingSearcher;
    private IReadableCharArray mockCharArray;

    @Override
    public void setUp()
    {
        super.setUp();
        this.mockNode = newMock(INode.class);
        this.mockCharArray = newMock(IReadableCharArray.class);
        this.findingSearcher = new FindingSearcher();
    }

    @Test
    public void testSearchForFindingNullNode()
    {
        expect(mockCharArray.get(2)).andReturn('e');
        expect(mockNode.findEdge('e')).andReturn(null);

        replayAll();

        Assert.assertNull(null, this.findingSearcher.search("text", mockCharArray, mockNode, 2));
    }

    @Test
    public void testSearchForFindingNotEmpty()
    {
        IEdge mockEdge = newMock(IEdge.class);

        expect(mockCharArray.get(1)).andReturn('t');
        expect(mockNode.findEdge('t')).andReturn(mockEdge);

        replayAll();

        Assert.assertEquals(mockEdge, this.findingSearcher.search("text", mockCharArray, this.mockNode, 1));
    }
}
