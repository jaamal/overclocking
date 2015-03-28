package tests.unit.CompressingCore.helpers.compressingCore.dataFiltering.automatas;

import compressingCore.dataFiltering.automatas.AlphabiteSeparator;
import compressingCore.dataFiltering.automatas.IAlphabite;

import org.junit.Test;

import tests.unit.UnitTestBase;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AlphabiteSeparatorTest extends UnitTestBase
{
    private IAlphabite alphabite;
    private AlphabiteSeparator automata;

    @Override
    public void setUp()
    {
        super.setUp();
        alphabite = newMock(IAlphabite.class);
        automata = new AlphabiteSeparator(alphabite);
    }

    @Test
    public void testStartState()
    {
        replay(alphabite);

        assertTrue(automata.isAcceptState());
        automata.moveToStart();
        assertTrue(automata.isAcceptState());
    }

    @Test
    public void testMoveUsingNotEscapableSymbol()
    {
        expect(alphabite.contains('a')).andReturn(false);
        replay(alphabite);

        assertTrue(automata.isAcceptState());
        automata.move('a');
        assertTrue(automata.isAcceptState());
    }

    @Test
    public void testMoveUsingEscapableSymbol()
    {
        expect(alphabite.contains('b')).andReturn(true);
        replay(alphabite);

        assertTrue(automata.isAcceptState());
        automata.move('b');
        assertFalse(automata.isAcceptState());
    }
}
