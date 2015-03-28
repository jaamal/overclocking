package tests.integration.Algorithms;

import helpers.FileHelpers;

import java.io.IOException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.DebuggingAPTable;
import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.builder.APTableBuilderFactory;
import patternMatching.fcpm.table.builder.APTableType;
import patternMatching.fcpm.table.builder.ExecutionStrategy;
import patternMatching.fcpm.table.builder.IAPTableBuilder;
import patternMatching.fcpm.table.builder.IPatternMatchingConfig;
import patternMatching.fcpm.table.builder.LocalSearchStrategy;
import patternMatching.fcpm.table.builder.TextsIterationStrategy;
import tests.integration.IntegrationTestBase;
import tests.unit.Algorithms.patternMatching.SLPBuildHelper;

import commons.settings.ISettings;

public class FcmpTest extends IntegrationTestBase
{

    private SLPBuildHelper builder;
    private IAPTableBuilder tableBuilder;
    private Random random;

    @Before
    public void setUp()
    {
        super.setUp();
        builder = container.create(SLPBuildHelper.class);
        random = new Random(System.nanoTime());
        IPatternMatchingConfig config = new IPatternMatchingConfig()
        {

            @Override
            public ExecutionStrategy getExecutionStrategy()
            {
                return ExecutionStrategy.MultiThreadWithAggregation;
            }

            @Override
            public int getThreadCount()
            {
                return 1;
            }

            @Override
            public APTableType getAPTableType()
            {
                return APTableType.OwnHashTable;
            }

            @Override
            public TextsIterationStrategy getTextsIterationStrategy() {
                return TextsIterationStrategy.Ordered;
            }

            @Override
            public boolean withStatistics() {
                return true;
            }

            @Override
            public LocalSearchStrategy getLocalSearchStrategy() {
                return LocalSearchStrategy.Recursive;
            }
        };

        APTableBuilderFactory tableBuilderFactory = new APTableBuilderFactory();

        tableBuilder = tableBuilderFactory.create(config);
    }

    private void doTestDNAFromFile(String dnaName) throws IOException
    {
        String text = FileHelpers.readDNA(container, dnaName);
        System.out.println("===");
        System.out.println("Text: " + dnaName);
        doTest(text);
    }

    private void doTest(String text)
    {
        System.out.println("TextLength: " + text.length());
        Product[] textSlp = builder.buildSlp(container.get(ISettings.class), text);
        System.out.println("TextSlpSize: " + textSlp.length);
        //doTest(textSlp, text, 1);
        //doTest(textSlp, text, 2);
        doTest(textSlp, text, 5);
        doTest(textSlp, text, 10);
        doTest(textSlp, text, 20);
        doTest(textSlp, text, 50);
        doTest(textSlp, text, 100);
        doTest(textSlp, text, 200);
        doTest(textSlp, text, 500);
        doTest(textSlp, text, 1000);
        doTest(textSlp, text, 2000);
        //doTest(textSlp, text, 5000);
        //doTest(textSlp, text, 10000);
    }

    private void doTest(Product[] textSlp, String text, int patternSize)
    {
        int length = text.length();
        String pattern = text.substring(0, length / patternSize);
        System.out.println("---");
        System.out.println("PatternLength: " + pattern.length());
        Product[] patternSlp = builder.buildSlp(container.get(ISettings.class), pattern);
        System.out.println("PatternSlpSize: " + patternSlp.length);
        IAPTable table = tableBuilder.build(patternSlp, textSlp);
        if (table instanceof DebuggingAPTable)
        {
            DebuggingAPTable debuggingAPTable = (DebuggingAPTable) table;
            double emptyCount = debuggingAPTable.statistics().emptyCount();
            double totalCount = debuggingAPTable.statistics().totalCount();
            System.out.println(String.format("Emptiness is " + (emptyCount ) / totalCount ));
        }
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

    private void doTestWithRandom2(int length)
    {
        System.out.println("===");
        System.out.println("Random2");
        doTest(getRandom2(length));
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
        /*doTestWithRandom26(1500000);
        doTestWithRandom26(750000);
        doTestWithRandom26(500000);
        doTestWithRandom26(250000);
        doTestWithRandom26(100000);
        doTestWithRandom26(50000);*/
        doTestWithRandom26(25000);
        doTestWithRandom2(25000);
        doTestWithRandom26(10000);
        doTestWithRandom2(10000);
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

    private String getRandom2(int length)
    {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i)
        {
            builder.append((char) (random.nextInt(1) + 'a'));
        }
        return builder.toString();
    }

}
