package tests.unit.CompressionService.lz77;

import static org.easymock.EasyMock.expect;
import junit.framework.Assert;

import org.junit.Test;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISuffixPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.SuffixLinker;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISuffixPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import tests.unit.UnitTestBase;

public class SuffixLinkerTest extends UnitTestBase
{
    private SuffixLinker suffixLinker;
    private ISuffixPlaceFactory mockSuffixPlaceFactory;

    @Override
    public void setUp()
    {
        super.setUp();
        this.mockSuffixPlaceFactory = newMock(ISuffixPlaceFactory.class);
        this.suffixLinker = new SuffixLinker(this.mockSuffixPlaceFactory);
    }

    @Test
    public void testCreateSuffixLinkNullNode()
    {
        ISuffixPlace mockSuffixPlace = newMock(ISuffixPlace.class);
        INode mockNode = newMock(INode.class);
        IInsertPlace mockInsertPlace = newMock(IInsertPlace.class);
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNewNode = newMock(INode.class);

        expect(mockSuffixPlace.extension()).andReturn(1).anyTimes();
        expect(mockSuffixPlace.getNode()).andReturn(mockNode).anyTimes();
        expect(mockInsertPlace.getEdge()).andReturn(mockEdge).anyTimes();
        expect(mockEdge.endNode()).andReturn(mockNewNode).anyTimes();
        mockNode.setSuffixLink(mockNewNode);

        replayAll();

        Assert.assertEquals(suffixLinker.createSuffixLink(mockSuffixPlace, null, mockInsertPlace, 2), mockSuffixPlace);
    }

    @Test
    public void testCreateSuffixLinkNotNullNode()
    {
        ISuffixPlace mockSuffixPlace = newMock(ISuffixPlace.class);
        INode mockNode = newMock(INode.class);
        IInsertPlace mockInsertPlace = newMock(IInsertPlace.class);
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNewNode = newMock(INode.class);
        INode mockNewNode2 = newMock(INode.class);

        expect(mockSuffixPlace.extension()).andReturn(1).anyTimes();
        expect(mockSuffixPlace.getNode()).andReturn(mockNode).anyTimes();
        mockNode.setSuffixLink(mockNewNode2);
        expect(mockInsertPlace.getNode()).andReturn(mockNewNode).anyTimes();
        expect(mockNewNode.getFatherEdge()).andReturn(mockEdge).anyTimes();
        expect(mockSuffixPlaceFactory.create(2, mockNewNode2)).andReturn(mockSuffixPlace).anyTimes();

        replayAll();

        Assert.assertEquals(suffixLinker.createSuffixLink(mockSuffixPlace, mockNewNode2, mockInsertPlace, 2), mockSuffixPlace);
    }

    @Test
    public void testCreateSuffixLinkToRoot()
    {
        ISuffixPlace mockSuffixPlace = newMock(ISuffixPlace.class);
        IInsertPlace mockInsertPlace = newMock(IInsertPlace.class);
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNewNode = newMock(INode.class);
        INode mockNewNode2 = newMock(INode.class);
        IEdge mockEdge2 = newMock(IEdge.class);

        expect(mockSuffixPlace.extension()).andReturn(2).anyTimes();
        expect(mockInsertPlace.getNode()).andReturn(mockNewNode).anyTimes();
        expect(mockNewNode.getFatherEdge()).andReturn(mockEdge2).anyTimes();
        expect(mockNewNode2.getFatherEdge()).andReturn(mockEdge).anyTimes();
        expect(mockEdge.beginPosition()).andReturn(2).anyTimes();
        expect(mockEdge.endPosition()).andReturn(2).anyTimes();
        expect(mockNewNode2.getSuffixLink()).andReturn(mockNewNode).anyTimes();
        expect(mockSuffixPlaceFactory.create(2, mockNewNode2)).andReturn(mockSuffixPlace);

        replayAll();

        Assert.assertEquals(suffixLinker.createSuffixLink(mockSuffixPlace, mockNewNode2, mockInsertPlace, 2), mockSuffixPlace);
    }

    @Test
    public void testCreateSuffixLinkNotToRoot()
    {
        ISuffixPlace mockSuffixPlace = newMock(ISuffixPlace.class);
        IInsertPlace mockInsertPlace = newMock(IInsertPlace.class);
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNewNode = newMock(INode.class);
        INode mockNewNode2 = newMock(INode.class);

        expect(mockSuffixPlace.extension()).andReturn(2).anyTimes();
        expect(mockInsertPlace.getNode()).andReturn(mockNewNode).anyTimes();
        expect(mockNewNode.getFatherEdge()).andReturn(mockEdge).anyTimes();
        expect(mockSuffixPlaceFactory.create(2, mockNewNode2)).andReturn(mockSuffixPlace);

        replayAll();

        Assert.assertEquals(suffixLinker.createSuffixLink(mockSuffixPlace, mockNewNode2, mockInsertPlace, 2), mockSuffixPlace);
    }

    @Test
    public void testNotCreateSuffixLink()
    {
        ISuffixPlace mockSuffixPlace = newMock(ISuffixPlace.class);
        IInsertPlace mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockSuffixPlace.extension()).andReturn(2).anyTimes();

        replayAll();

        Assert.assertEquals(suffixLinker.createSuffixLink(mockSuffixPlace, null, mockInsertPlace, 2), mockSuffixPlace);
    }
}
