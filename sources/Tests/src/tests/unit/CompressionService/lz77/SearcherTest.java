package tests.unit.CompressionService.lz77;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.expect;

import org.junit.Before;
import org.junit.Test;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.Searcher;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IBeginPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import tests.unit.UnitTestBase;


public class SearcherTest extends UnitTestBase
{
    private Searcher searcher;
    private IBeginPlaceFactory mockBeginPlaceFactory;

    @Before
    public void setUp()
    {
        super.setUp();
        this.mockBeginPlaceFactory = newMock(IBeginPlaceFactory.class);
        this.searcher = new Searcher();
    }

    @Test
    public void testSearchEndNotRootAndNullSuffixLink()
    {
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNode = newMock(INode.class);
        IEdge mockNewEdge = newMock(IEdge.class);
        INode mockNodeNew1 = newMock(INode.class);
        IBeginPlace mockBeginPlace = newMock(IBeginPlace.class);

        expect(mockEdge.beginNode()).andReturn(mockNode).anyTimes();
        expect(mockNode.getSuffixLink()).andReturn(null).anyTimes();
        expect(mockNode.getFatherEdge()).andReturn(mockNewEdge).anyTimes();
        expect(mockNewEdge.beginNode()).andReturn(mockNodeNew1).anyTimes();
        expect(mockNewEdge.beginPosition()).andReturn(1).anyTimes();
        expect(mockNewEdge.endPosition()).andReturn(2).anyTimes();
        expect(mockBeginPlaceFactory.create(mockNodeNew1, 1, 2)).andReturn(mockBeginPlace);

        replayAll();

        assertEquals(searcher.searchEnd(mockEdge, 1, mockBeginPlaceFactory), mockBeginPlace);
    }

    @Test
    public void testSearchEndRootNodeOrSuffixLink()
    {
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNode = newMock(INode.class);
        INode mockNodeNew2 = newMock(INode.class);
        IBeginPlace mockBeginPlace = newMock(IBeginPlace.class);

        expect(mockEdge.beginNode()).andReturn(mockNode).anyTimes();
        expect(mockNode.getSuffixLink()).andReturn(mockNodeNew2).anyTimes();
        expect(mockNode.getFatherEdge()).andReturn(null).anyTimes();
        expect(mockEdge.beginPosition()).andReturn(1).anyTimes();
        expect(mockEdge.endPosition()).andReturn(2).anyTimes();
        expect(mockBeginPlaceFactory.create(mockNode, 1, 2)).andReturn(mockBeginPlace).anyTimes();

        replayAll();

        assertEquals(searcher.searchEnd(mockEdge, 1, mockBeginPlaceFactory), mockBeginPlace);
    }

}
