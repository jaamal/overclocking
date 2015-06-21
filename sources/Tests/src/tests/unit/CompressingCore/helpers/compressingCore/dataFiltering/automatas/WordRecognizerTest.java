package tests.unit.CompressingCore.helpers.compressingCore.dataFiltering.automatas;

import org.junit.Test;

import data.filters.automatas.WordRecognizer;
import tests.unit.UnitTestBase;
import static org.junit.Assert.*;

public class WordRecognizerTest extends UnitTestBase
{
    private final static String WORD = "wOrD";
    private WordRecognizer automata;

    @Test
    public void testAcceptWORD()
    {
        automata = new WordRecognizer(WORD);
        doTest(WORD, 1);
    }

    @Test
    public void testTwoWORDs()
    {
        automata = new WordRecognizer(WORD);
        doTest(WORD + WORD, 2);
    }

    @Test
    public void testTrashAtStart()
    {
        automata = new WordRecognizer(WORD);
        doTest("wor" + WORD, 1);
        automata.moveToStart();
        doTest("z" + WORD + "wor" + WORD + "z", 2);
    }

    @Test
    public void testComplexWord()
    {
        automata = new WordRecognizer("abAbaBB");
        doTest("aBabAbAbabb", "abaBabb", 1);
    }

    @Test
    public void testSearchComplexWordSeveralTimes()
    {
        automata = new WordRecognizer("AbabaB");
        doTest("AbababababB", "aBaBaB", 3);
    }

    @Test
    public void testSearchSimpleWordSeveralTimes()
    {
        automata = new WordRecognizer("AaA");
        doTest("aaaaaaa", "aAa", 5);
    }

    private void doTest(String word, int expectedCount)
    {
        doTest(word, WORD, expectedCount);
    }

    private void doTest(String word, String pattern, int expectedCount)
    {
        word = word.toLowerCase();
        pattern = pattern.toLowerCase();
        assertFalse(automata.isAcceptState());
        int actualAccepteds = 0;
        for (int i = 0; i < word.length(); i++)
        {
            automata.move(word.charAt(i));
            if (i < pattern.length() - 1)
                assertFalse(automata.isAcceptState());
            else
            {
                if (word.substring(i - pattern.length() + 1, i + 1).equals(pattern))
                {
                    assertTrue(automata.isAcceptState());
                    actualAccepteds++;
                } else
                    assertFalse(automata.isAcceptState());
            }
        }
        assertEquals(expectedCount, actualAccepteds);
    }
}
