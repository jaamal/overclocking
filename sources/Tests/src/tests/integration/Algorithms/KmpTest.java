package tests.integration.Algorithms;

import static org.junit.Assert.assertTrue;
import helpers.FileHelpers;

import java.io.IOException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import patternMatching.IPatternMatcher;
import patternMatching.kmp.KMPMatcher;
import tests.integration.IntegrationTestBase;

public class KmpTest extends IntegrationTestBase
{

    private Random random;

    @Before
    public void setUp()
    {
        super.setUp();
        random = new Random(System.nanoTime());
    }

    private void doTestDNAFromFile(String dnaName) throws IOException
    {
        String text = FileHelpers.readDNA(container, dnaName);
        System.out.println("===");
        System.out.println("Text: " + dnaName);
        doTest(text);
    }

    private static void doTest(String text)
    {
        System.out.println("TextLength: " + text.length());
        //doTest(textSlp, text, 1);
        //doTest(textSlp, text, 2);
        //doTest(textSlp, text, 10);
        //doTest(textSlp, text, 20);
        //doTest(textSlp, text, 50);
        doTest(text, 100);
        doTest(text, 200);
        doTest(text, 500);
        doTest(text, 1000);
        doTest(text, 2000);
        doTest(text, 5000);
        doTest(text, 10000);
    }

    private static void doTest(String text, int patternSize)
    {
        int length = text.length();
        String pattern = text.substring(0, length / patternSize);
        System.out.println("---");
        System.out.println("PatternLength: " + pattern.length());

        IPatternMatcher matcher = new KMPMatcher(text, pattern);
        long matchingBegin = System.currentTimeMillis();
        assertTrue(matcher.contains());

        long matchingEnd = System.currentTimeMillis();
        long matchingTime = (matchingEnd - matchingBegin) / 1000;
        System.out.println("MatchTime: " + matchingTime + " s");

    }

    @Test
    public void testDNA() throws IOException
    {
        doTestDNAFromFile("AAZR.gz");
        doTestDNAFromFile("AAZS.gz");
        doTestDNAFromFile("AAYP.gz");
        doTestDNAFromFile("AAYN.gz");
        doTestDNAFromFile("AAVR.gz");
        doTestDNAFromFile("AAEJ.gz");
        doTestDNAFromFile("AAZT.gz");
        doTestDNAFromFile("AAUY.gz");
        doTestDNAFromFile("AAYO.gz");
        doTestDNAFromFile("AADJ.gz");
        doTestDNAFromFile("AAXS.gz");
        doTestDNAFromFile("AAFE.gz");
        doTestDNAFromFile("AAZQ.gz");
        doTestDNAFromFile("AAMI.gz");
        doTestDNAFromFile("AAFF.gz");
        doTestDNAFromFile("AAZK.gz");
        doTestDNAFromFile("AAMG.gz");
        doTestDNAFromFile("AAQP.gz");
        doTestDNAFromFile("AADU.gz");
        doTestDNAFromFile("AAYM.gz");
        doTestDNAFromFile("AABV.gz");
        doTestDNAFromFile("AAGX.gz");
        doTestDNAFromFile("AACL.gz");
        doTestDNAFromFile("AANL.gz");
        doTestDNAFromFile("AABT.gz");
        doTestDNAFromFile("AAZV.gz");
        doTestDNAFromFile("AAES.gz");
        doTestDNAFromFile("AAEZ.gz");
        doTestDNAFromFile("AACR.gz");
    }

    private void doTestWithRandomDNA(int length)
    {
        System.out.println("===");
        System.out.println("RandomDNA");
        doTest(getRandomDNA(length));
    }

    private void doTestWithRandom26(int length)
    {
        System.out.println("===");
        System.out.println("Random26");
        doTest(getRandom26(length));
    }

    @Test
    public void testRandomDNA()
    {
        doTestWithRandomDNA(1500000);
        doTestWithRandomDNA(750000);
        doTestWithRandomDNA(500000);
        doTestWithRandomDNA(250000);
        doTestWithRandomDNA(100000);
        doTestWithRandomDNA(50000);
        doTestWithRandomDNA(25000);
        doTestWithRandomDNA(10000);
    }

    @Test
    public void testRandom26()
    {
        doTestWithRandom26(1500000);
        doTestWithRandom26(750000);
        doTestWithRandom26(500000);
        doTestWithRandom26(250000);
        doTestWithRandom26(100000);
        doTestWithRandom26(50000);
        doTestWithRandom26(25000);
        doTestWithRandom26(10000);
    }

    private String getRandomDNA(int length)
    {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i)
        {
            builder.append("AGCT".charAt(random.nextInt(4)));
        }
        return builder.toString();
    }


    private String getRandom26(int length)
    {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i)
        {
            builder.append((char) (random.nextInt(25) + 'a'));
        }
        return builder.toString();
    }

}
