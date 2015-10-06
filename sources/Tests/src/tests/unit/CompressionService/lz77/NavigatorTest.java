package tests.unit.CompressionService.lz77;

import static org.easymock.EasyMock.expect;
import org.junit.Test;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IBeginPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.Navigator;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.IInsertPlaceFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import junit.framework.Assert;
import tests.unit.UnitTestBase;

public class NavigatorTest extends UnitTestBase
{
    private IInsertPlaceFactory mockInsertPlaceFactory;
    private Navigator navigator;
    private String text;

    @Override
    public void setUp()
    {
        super.setUp();
        this.mockInsertPlaceFactory = newMock(IInsertPlaceFactory.class);
        this.text = "text";
        this.navigator = new Navigator(this.text, this.mockInsertPlaceFactory);
    }

    @Test
    public void testGetPathRootNode()
    {
        IBeginPlace mockBeginPlace = newMock(IBeginPlace.class);
        INode mockNode = newMock(INode.class);
        IEdge mockEdge = newMock(IEdge.class);
        IInsertPlace mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockBeginPlace.getNode()).andReturn(mockNode);
        expect(mockNode.getFatherEdge()).andReturn(null);
        expect(mockNode.findEdge('t')).andReturn(mockEdge);
        expect(mockEdge.fromPosition()).andReturn(0).anyTimes();
        expect(mockEdge.toPosition()).andReturn(2).anyTimes();
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
        IEdge mockEdge = newMock(IEdge.class);
        IInsertPlace mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockBeginPlace.getNode()).andReturn(mockNode1).anyTimes();
        expect(mockNode1.getFatherEdge()).andReturn(mockEdge1).anyTimes();
        expect(mockNode1.getSuffixLink()).andReturn(mockNode).anyTimes();
        expect(mockBeginPlace.beginPosition()).andReturn(1).anyTimes();
        expect(mockBeginPlace.endPosition()).andReturn(3).anyTimes();
        expect(mockNode.findEdge('e')).andReturn(mockEdge);
        expect(mockEdge.fromPosition()).andReturn(1).anyTimes();
        expect(mockEdge.toPosition()).andReturn(4).anyTimes();
        expect(mockInsertPlaceFactory.create(mockNode, mockEdge, 2)).andReturn(mockInsertPlace);

        replayAll();

        Assert.assertEquals(navigator.getPath(mockBeginPlace, 0, 1), mockInsertPlace);
    }

}
