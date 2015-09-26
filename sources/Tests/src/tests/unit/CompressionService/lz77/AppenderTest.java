package tests.unit.CompressionService.lz77;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.expect;

import org.junit.Test;

import compressionservice.algorithms.lz77.suffixTree.creatingTree.Appender;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.IInsertPlace;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.ISearcher;
import compressionservice.algorithms.lz77.suffixTree.creatingTree.factories.ISearcherFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.IEdgeFactory;
import compressionservice.algorithms.lz77.suffixTree.structures.factories.INodeFactory;
import tests.unit.UnitTestBase;

public class AppenderTest extends UnitTestBase
{
    private Appender appender;
    private ISearcherFactory mockSearcherFactory;
    private IEdgeFactory mockEdgeFactory;
    private INodeFactory mockNodeFactory;
    private IInsertPlace mockInsertPlace;
    private INode mockRoot;

    @Override
    public void setUp()
    {
        super.setUp();
        this.mockSearcherFactory = newMock(ISearcherFactory.class);
        this.mockEdgeFactory = newMock(IEdgeFactory.class);
        this.mockNodeFactory = newMock(INodeFactory.class);
        this.appender = new Appender(mockSearcherFactory, mockEdgeFactory, mockNodeFactory);
    }

    @Test
    public void testAddSymbolNotCreateLeaf()
    {
        ISearcher mockSearcher = newMock(ISearcher.class);
        IEdge mockEdge = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockSearcherFactory.create()).andReturn(mockSearcher).anyTimes();
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
        ISearcher mockSearcher = newMock(ISearcher.class);
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNode = newMock(INode.class);
        IEdge mockNewEdge = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockSearcherFactory.create()).andReturn(mockSearcher).anyTimes();
        expect(mockInsertPlace.getEdge()).andReturn(mockEdge).anyTimes();
        expect(mockEdge.fromPosition()).andReturn(6).anyTimes();
        expect(mockInsertPlace.getPosition()).andReturn(2).anyTimes();
        expect(mockEdge.toPosition()).andReturn(8).anyTimes();
        expect(mockEdge.toNode()).andReturn(mockNode).anyTimes();
        expect(mockSearcher.search("texttext", mockNode, 4)).andReturn(null).anyTimes();
        expect(mockEdgeFactory.createLeaf(4, mockNode, 1)).andReturn(mockNewEdge).anyTimes();
        mockNode.addEdge('t', mockNewEdge);

        replayAll();

        INode node = appender.append("texttext", mockInsertPlace, 4, 1);

        assertEquals(null, node);
        assertEquals(false, appender.isImplicitExtension());
    }

    @Test
    public void testAddSymbolImplicitExtension()
    {
        ISearcher mockSearcher = newMock(ISearcher.class);
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNode = newMock(INode.class);
        IEdge mockNewEdge = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockSearcherFactory.create()).andReturn(mockSearcher).anyTimes();
        expect(mockInsertPlace.getEdge()).andReturn(mockEdge).anyTimes();
        expect(mockEdge.fromPosition()).andReturn(6).anyTimes();
        expect(mockInsertPlace.getPosition()).andReturn(2).anyTimes();
        expect(mockEdge.toPosition()).andReturn(8).anyTimes();
        expect(mockEdge.toNode()).andReturn(mockNode).anyTimes();
        expect(mockSearcher.search("texttext", mockNode, 9)).andReturn(mockNewEdge).anyTimes();

        replayAll();

        INode node = appender.append("texttext", mockInsertPlace, 9, 1);

        assertEquals(null, node);
        assertEquals(true, appender.isImplicitExtension());
    }

    @Test
    public void testAddNode()
    {
        ISearcher mockSearcher = newMock(ISearcher.class);
        IEdge mockEdge = newMock(IEdge.class);
        INode mockNodeNew = newMock(INode.class);
        INode mockBeginNode = newMock(INode.class);
        IEdge mockNewEdge1 = newMock(IEdge.class);
        IEdge mockNewEdge2 = newMock(IEdge.class);
        IEdge mockLeaf = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockSearcherFactory.create()).andReturn(mockSearcher).anyTimes();
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
        mockNodeNew.addEdge('e', mockLeaf);
        mockNodeNew.addEdge('w', mockNewEdge2);
        mockBeginNode.changeEdge('x', mockNewEdge1);
        mockInsertPlace.changeEdge(mockNewEdge1);

        replayAll();

        INode node = appender.append("texttextqweru", mockInsertPlace, 10, 1);

        assertEquals(mockNodeNew, node);
        assertEquals(false, appender.isImplicitExtension());
    }

    @Test
    public void testDoNothing()
    {
        ISearcher mockSearcher = newMock(ISearcher.class);
        IEdge mockEdge = newMock(IEdge.class);

        mockInsertPlace = newMock(IInsertPlace.class);

        expect(mockSearcherFactory.create()).andReturn(mockSearcher).anyTimes();
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
        ISearcher mockSearcher = newMock(ISearcher.class);
        IEdge mockEdge = newMock(IEdge.class);

        mockRoot = newMock(INode.class);

        expect(mockSearcherFactory.create()).andReturn(mockSearcher).anyTimes();
        expect(mockSearcher.search("text", mockRoot, 2)).andReturn(null).anyTimes();
        expect(mockEdgeFactory.createLeaf(2, mockRoot, 2)).andReturn(mockEdge).anyTimes();
        mockRoot.addEdge('x', mockEdge);

        replayAll();

        appender.append("text", mockRoot, 2);

        assertEquals(false, appender.isImplicitExtension());
    }

    @Test
    public void testAppendNodeNotAdd()
    {
        ISearcher mockSearcher = newMock(ISearcher.class);
        IEdge edge = newMock(IEdge.class);

        mockRoot = newMock(INode.class);

        expect(mockSearcherFactory.create()).andReturn(mockSearcher).anyTimes();
        expect(mockSearcher.search("text", mockRoot, 2)).andReturn(edge).anyTimes();

        replayAll();

        appender.append("text", mockRoot, 2);

        assertEquals(true, appender.isImplicitExtension());
    }
}
