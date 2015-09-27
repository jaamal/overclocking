package tests.unit.CompressionService.lz77;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.expect;
import org.junit.Test;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.Appender;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.INodeFactory;
import tests.unit.UnitTestBase;

public class AppenderTest extends UnitTestBase
{
    private Appender appender;
    private IEdgeFactory mockEdgeFactory;
    private INodeFactory mockNodeFactory;
    private IInsertPlace mockInsertPlace;
    private INode mockRoot;

    @Override
    public void setUp()
    {
        super.setUp();
        this.mockEdgeFactory = newMock(IEdgeFactory.class);
        this.mockNodeFactory = newMock(INodeFactory.class);
        this.appender = new Appender(mockEdgeFactory, mockNodeFactory);
    }

    @Test
    public void testAddSymbolNotCreateLeaf()
    {
        IEdge mockEdge = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockInsertPlace.getEdge()).andReturn(mockEdge).anyTimes();
        expect(mockEdge.fromPosition()).andReturn(6).anyTimes();
        expect(mockInsertPlace.getPosition()).andReturn(2).anyTimes();
        expect(mockEdge.toPosition()).andReturn(8).anyTimes();
        expect(mockEdge.toNode()).andReturn(null).anyTimes();

        replayAll();

        INode node = appender.append("qwertyuqw", mockInsertPlace, 9, 1);

        assertEquals(null, node);
        assertEquals(false, appender.isImplicitExtension());
    }

    @Test
    public void testAddSymbolCreateLeaf()
    {
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNode = newMock(INode.class);
        IEdge mockNewEdge = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockInsertPlace.getEdge()).andReturn(mockEdge).anyTimes();
        expect(mockEdge.fromPosition()).andReturn(6).anyTimes();
        expect(mockInsertPlace.getPosition()).andReturn(2).anyTimes();
        expect(mockEdge.toPosition()).andReturn(8).anyTimes();
        expect(mockEdge.toNode()).andReturn(mockNode).anyTimes();
        expect(mockNode.findEdge('t')).andReturn(null);
        expect(mockEdgeFactory.createLeaf(4, mockNode, 1)).andReturn(mockNewEdge).anyTimes();
        mockNode.putEdge('t', mockNewEdge);

        replayAll();

        INode node = appender.append("texttext", mockInsertPlace, 4, 1);

        assertEquals(null, node);
        assertEquals(false, appender.isImplicitExtension());
    }

    @Test
    public void testAddSymbolImplicitExtension()
    {
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNode = newMock(INode.class);
        IEdge mockNewEdge = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockInsertPlace.getEdge()).andReturn(mockEdge).anyTimes();
        expect(mockEdge.fromPosition()).andReturn(6).anyTimes();
        expect(mockInsertPlace.getPosition()).andReturn(2).anyTimes();
        expect(mockEdge.toPosition()).andReturn(8).anyTimes();
        expect(mockEdge.toNode()).andReturn(mockNode).anyTimes();
        expect(mockNode.findEdge('s')).andReturn(mockNewEdge);

        replayAll();

        INode node = appender.append("texttextss", mockInsertPlace, 9, 1);

        assertEquals(null, node);
        assertEquals(true, appender.isImplicitExtension());
    }

    @Test
    public void testAddNode()
    {
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNodeNew = newMock(INode.class);
        INode mockBeginNode = newMock(INode.class);
        IEdge mockNewEdge1 = newMock(IEdge.class);
        IEdge mockNewEdge2 = newMock(IEdge.class);
        IEdge mockLeaf = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockInsertPlace.getEdge()).andReturn(mockEdge).anyTimes();
        expect(mockEdge.fromPosition()).andReturn(6).anyTimes();
        expect(mockEdge.getNumber()).andReturn(1).anyTimes();
        expect(mockInsertPlace.getPosition()).andReturn(2).anyTimes();
        expect(mockEdge.toPosition()).andReturn(9).anyTimes();
        expect(mockEdge.fromNode()).andReturn(mockBeginNode).anyTimes();
        expect(mockNodeFactory.create()).andReturn(mockNodeNew);
        expect(mockEdgeFactory.createEdge(6, mockBeginNode, 8, mockNodeNew, 1)).andReturn(mockNewEdge1).anyTimes();
        expect(mockEdgeFactory.create(mockEdge, 9, mockNodeNew, 1)).andReturn(mockNewEdge2).anyTimes();
        mockNodeNew.setFatherEdge(mockNewEdge1);
        expect(mockEdgeFactory.createLeaf(10, mockNodeNew, 1)).andReturn(mockLeaf).anyTimes();
        mockNodeNew.putEdge('e', mockLeaf);
        mockNodeNew.putEdge('w', mockNewEdge2);
        mockBeginNode.putEdge('x', mockNewEdge1);
        mockInsertPlace.changeEdge(mockNewEdge1);

        replayAll();

        INode node = appender.append("texttextqweru", mockInsertPlace, 10, 1);

        assertEquals(mockNodeNew, node);
        assertEquals(false, appender.isImplicitExtension());
    }

    @Test
    public void testDoNothing()
    {
        IEdge mockEdge = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockInsertPlace.getEdge()).andReturn(mockEdge).anyTimes();
        expect(mockEdge.fromPosition()).andReturn(0).anyTimes();
        expect(mockEdge.toPosition()).andReturn(2).anyTimes();
        expect(mockInsertPlace.getPosition()).andReturn(0).anyTimes();

        replayAll();

        INode node = appender.append("xabxa", mockInsertPlace, 4, 1);

        assertEquals(null, node);
        assertEquals(true, appender.isImplicitExtension());
    }

    @Test
    public void testAppendNodeAdd()
    {
        IEdge mockEdge = newMock(IEdge.class);
        mockRoot = newMock(INode.class);

        expect(mockRoot.findEdge('x')).andReturn(null);
        expect(mockEdgeFactory.createLeaf(2, mockRoot, 2)).andReturn(mockEdge).anyTimes();
        mockRoot.putEdge('x', mockEdge);

        replayAll();

        appender.append("text", mockRoot, 2);

        assertEquals(false, appender.isImplicitExtension());
    }

    @Test
    public void testAppendNodeNotAdd()
    {
        IEdge edge = newMock(IEdge.class);

        mockRoot = newMock(INode.class);

        expect(mockRoot.findEdge('x')).andReturn(edge);

        replayAll();

        appender.append("text", mockRoot, 2);
        assertEquals(true, appender.isImplicitExtension());
    }
}
