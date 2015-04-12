package tests.unit.CompressionService.lz77;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tests.unit.UnitTestBase;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.factorization.LZ77FactorIterator;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Location;
import compressionservice.compression.algorithms.lz77.windows.IStringWindow;
import compressionservice.compression.algorithms.lz77.windows.IWindowFactory;

import dataContracts.FactorDef;

public class LZ77FactorIteratorTest extends UnitTestBase
{
    private final static int WINDOW_SIZE = 10;
    private IReadableCharArray charArray;
    private IWindowFactory windowFactory;
    private IStringWindow window;
    private LZ77FactorIterator factorIterator;

    public void setUp()
    {
        super.setUp();
        this.windowFactory = newMock(IWindowFactory.class);
        this.charArray = newMock(IReadableCharArray.class);
        this.window = newMock(IStringWindow.class);
    }

    @Test
    public void testEmptyFactorizator()
    {
        expect(windowFactory.create(WINDOW_SIZE)).andReturn(window);
        expect(charArray.length()).andReturn(0L);

        replayAll();

        this.factorIterator = new LZ77FactorIterator(windowFactory, this.charArray, WINDOW_SIZE);
        assertFalse(this.factorIterator.any());
    }

    @Test(expected = IllegalAccessError.class)
    public void testNextFactorWithException()
    {
        expect(windowFactory.create(WINDOW_SIZE)).andReturn(window);
        expect(charArray.length()).andReturn(0L);

        replayAll();

        this.factorIterator = new LZ77FactorIterator(windowFactory, this.charArray, WINDOW_SIZE);
        this.factorIterator.next();
    }

    @Test
    public void testSingleFactor()
    {
        IReadableCharArray subString = newMock(IReadableCharArray.class);
        Location location = Location.create(0, 0);

        expect(windowFactory.create(WINDOW_SIZE)).andReturn(window);
        expect(charArray.length()).andReturn(1L).anyTimes();
        expect(charArray.subArray(0, 1)).andReturn(subString);
        expect(window.search(subString)).andReturn(location);
        expect(charArray.get(0)).andReturn('a').anyTimes();
        window.append("a");

        replayAll();

        this.factorIterator = new LZ77FactorIterator(windowFactory, this.charArray, WINDOW_SIZE);
        assertTrue(this.factorIterator.any());
        FactorDef actual = this.factorIterator.next();
        assertTrue(actual.isTerminal);
        assertEquals('a', actual.symbol);
        assertFalse(this.factorIterator.any());
    }

    @Test
    public void testNotSingleFactor()
    {
        IReadableCharArray subString = newMock(IReadableCharArray.class);
        Location location = Location.create(0, 10);
        
        expect(windowFactory.create(WINDOW_SIZE)).andReturn(window);
        expect(charArray.length()).andReturn(11L).anyTimes();
        expect(charArray.subArray(0, 10)).andReturn(subString).anyTimes();
        expect(window.search(subString)).andReturn(location);
        expect(charArray.toString(0, 10)).andReturn("asdfgasdfg");
        window.append("asdfgasdfg");

        replayAll();

        this.factorIterator = new LZ77FactorIterator(windowFactory, this.charArray, WINDOW_SIZE);
        assertTrue(this.factorIterator.any());
        FactorDef actual = this.factorIterator.next();
        assertEquals(0, actual.beginPosition);
        assertEquals(10, actual.length);
        assertTrue(this.factorIterator.any());
    }
}
