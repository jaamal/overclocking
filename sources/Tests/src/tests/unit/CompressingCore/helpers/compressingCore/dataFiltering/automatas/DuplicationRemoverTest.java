package tests.unit.CompressingCore.helpers.compressingCore.dataFiltering.automatas;


import compressingCore.dataFiltering.automatas.DuplicationRemover;
import compressingCore.dataFiltering.automatas.IAlphabite;

import org.junit.Test;

import tests.unit.UnitTestBase;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DuplicationRemoverTest extends UnitTestBase
{
    private IAlphabite alphabite;
    private DuplicationRemover automata;

    @Override
    public void setUp()
    {
        super.setUp();
        alphabite = newMock(IAlphabite.class);
        automata = new DuplicationRemover(alphabite);
    }

    @Test
    public void testMoveToStart()
    {
        assertTrue(automata.isAcceptState());
        automata.moveToStart();
        assertTrue(automata.isAcceptState());

        replay(alphabite);
    }

    @Test
    public void testAcceptOnlyFirstSymbolInSequence()
    {
        expectMove(' ', true);
        assertTrue(automata.isAcceptState());

        expectMove(' ', true);
        assertFalse(automata.isAcceptState());

        expectMove(' ', true);
        assertFalse(automata.isAcceptState());

        replay(alphabite);
    }

    @Test
    public void testExitFromIgnoreState()
    {
        expectMove(' ', true);
        assertTrue(automata.isAcceptState());

        expectMove(' ', true);
        assertFalse(automata.isAcceptState());

        expectMove('a', false);
        assertTrue(automata.isAcceptState());

        replay(alphabite);
    }

    @Test
    public void testRemoveDuplicationWithSeveralSymbols()
    {
        expectMove(' ', true);
        assertTrue(automata.isAcceptState());

        expectMove(' ', true);
        assertFalse(automata.isAcceptState());

        expectMove('\r', true);
        assertTrue(automata.isAcceptState());

        expectMove('\r', true);
        assertFalse(automata.isAcceptState());

        replay(alphabite);
    }

    private void expectMove(char symbol, boolean isDuplication)
    {
        expect(alphabite.contains(symbol)).andReturn(isDuplication);
        replay(alphabite);
        automata.move(symbol);
        reset(alphabite);
    }

}
