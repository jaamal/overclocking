package tests.unit.CompressionService.lz77;

import static org.easymock.EasyMock.expect;
import junit.framework.Assert;

import org.junit.Test;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISearcher;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.Navigator;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IIInsertPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import tests.unit.UnitTestBase;

public class NavigatorTest extends UnitTestBase
{
    private IIInsertPlaceFactory mockInsertPlaceFactory;
    private ISearcherFactory mockSearcherFactory;
    private Navigator navigator;
    private String text;

    @Override
    public void setUp()
    {
        super.setUp();
        this.mockInsertPlaceFactory = newMock(IIInsertPlaceFactory.class);
        this.mockSearcherFactory = newMock(ISearcherFactory.class);
        this.text = "text";
        this.navigator = new Navigator(this.text, this.mockInsertPlaceFactory, this.mockSearcherFactory);
    }

    @Test
    public void testGetPathRootNode()
    {
        IBeginPlace mockBeginPlace = newMock(IBeginPlace.class);
        INode mockNode = newMock(INode.class);
        ISearcher mockSearcher = newMock(ISearcher.class);
        IEdge mockEdge = newMock(IEdge.class);
        IInsertPlace mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockBeginPlace.getNode()).andReturn(mockNode).anyTimes();
        expect(mockNode.getFatherEdge()).andReturn(null).anyTimes();
        expect(mockSearcherFactory.create()).andReturn(mockSearcher);
        expect(mockSearcher.search("text", mockNode, 0)).andReturn(mockEdge);
        expect(mockEdge.beginPosition()).andReturn(0).anyTimes();
        expect(mockEdge.endPosition()).andReturn(2).anyTimes();
        expect(mockInsertPlaceFactory.create(mockNode, mockEdge, 1)).andReturn(mockInsertPlace);

        replayAll();

        Assert.assertEquals(navigator.getPath(mockBeginPlace, 0, 1), mockInsertPlace);
    }

    @Test
    public void testGetPathNotRootNode()
    {
        IBeginPlace mockBeginPlace = newMock(IBeginPlace.class);
        INode mockNode1 = newMock(INode.class);
        INode mockNode = newMock(INode.class);
        IEdge mockEdge1 = newMock(IEdge.class);
        ISearcher mockSearcher = newMock(ISearcher.class);
        IEdge mockEdge = newMock(IEdge.class);
        IInsertPlace mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockBeginPlace.getNode()).andReturn(mockNode1).anyTimes();
        expect(mockNode1.getFatherEdge()).andReturn(mockEdge1).anyTimes();
        expect(mockNode1.getSuffixLink()).andReturn(mockNode).anyTimes();
        expect(mockBeginPlace.beginPosition()).andReturn(1).anyTimes();
        expect(mockBeginPlace.endPosition()).andReturn(3).anyTimes();
        expect(mockSearcherFactory.create()).andReturn(mockSearcher);
        expect(mockSearcher.search("text", mockNode, 1)).andReturn(mockEdge);
        expect(mockEdge.beginPosition()).andReturn(1).anyTimes();
        expect(mockEdge.endPosition()).andReturn(4).anyTimes();
        expect(mockInsertPlaceFactory.create(mockNode, mockEdge, 2)).andReturn(mockInsertPlace);

        replayAll();

        Assert.assertEquals(navigator.getPath(mockBeginPlace, 0, 1), mockInsertPlace);
    }

}
