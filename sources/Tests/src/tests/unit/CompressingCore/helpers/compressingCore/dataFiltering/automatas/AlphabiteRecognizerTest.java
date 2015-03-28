package tests.unit.CompressingCore.helpers.compressingCore.dataFiltering.automatas;

import compressingCore.dataFiltering.automatas.AlphabiteRecognizer;
import compressingCore.dataFiltering.automatas.IAlphabite;

import org.junit.Test;

import tests.unit.UnitTestBase;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AlphabiteRecognizerTest extends UnitTestBase
{
    private IAlphabite alphabite;
    private AlphabiteRecognizer alphabiteRecognizer;

    @Override
    public void setUp()
    {
        super.setUp();
        alphabite = newMock(IAlphabite.class);
        alphabiteRecognizer = new AlphabiteRecognizer(alphabite);
    }

    @Test
    public void testMoveWithAlphabiteSymbol()
    {
        expect(alphabite.contains('a')).andReturn(true);
        replay(alphabite);

        assertFalse(alphabiteRecognizer.isAcceptState());
        alphabiteRecognizer.move('a');
        assertTrue(alphabiteRecognizer.isAcceptState());
    }

    @Test
    public void testMoveWithNotAlphabiteSymbol()
    {
        expect(alphabite.contains('a')).andReturn(false);
        replay(alphabite);

        assertFalse(alphabiteRecognizer.isAcceptState());
        alphabiteRecognizer.move('a');
        assertFalse(alphabiteRecognizer.isAcceptState());
    }

    @Test
    public void testMoveToStart()
    {
        expect(alphabite.contains('a')).andReturn(true);
        replay(alphabite);

        alphabiteRecognizer.move('a');
        assertTrue(alphabiteRecognizer.isAcceptState());
        alphabiteRecognizer.moveToStart();
        assertFalse(alphabiteRecognizer.isAcceptState());
    }

    @Test
    public void testIntegration()
    {
        expect(alphabite.contains('a')).andReturn(true).anyTimes();
        expect(alphabite.contains('b')).andReturn(true).anyTimes();
        for (char ch = 'a'; ch <= 'z'; ch++)
        {
            if (ch != 'a' && ch != 'b')
            {
                expect(alphabite.contains(ch)).andReturn(false).anyTimes();
            }
        }
        replay(alphabite);
        alphabiteRecognizer = new AlphabiteRecognizer(alphabite);

        String input = "abdfbfbdbasbasbasbdbndbasbsdjhsdf";
        assertFalse(alphabiteRecognizer.isAcceptState());
        for (int i = 0; i < input.length(); i++)
        {
            char character = input.charAt(i);
            alphabiteRecognizer.move(character);
            if (character == 'a' || character == 'b')
                assertTrue(alphabiteRecognizer.isAcceptState());
            else
                assertFalse(alphabiteRecognizer.isAcceptState());
        }
    }
}
