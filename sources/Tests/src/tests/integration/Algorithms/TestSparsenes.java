package tests.integration.Algorithms;

import helpers.FileHelpers;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import patternMatching.fcpm.preprocessing.Product;
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

public class TestSparsenes extends IntegrationTestBase
{

    private IAPTableBuilder tableBuilder;

    @Override
    @Before
    public void setUp()
    {
        super.setUp();

        IPatternMatchingConfig singleThread = new IPatternMatchingConfig()
        {

            @Override
            public ExecutionStrategy getExecutionStrategy()
            {
                return ExecutionStrategy.SingleThread;
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
                return null;
            }

            @Override
            public boolean withStatistics() {
                return false;
            }

            @Override
            public LocalSearchStrategy getLocalSearchStrategy() {
                return LocalSearchStrategy.Recursive;
            }
        };

        APTableBuilderFactory tableBuilderFactory = new APTableBuilderFactory();

        tableBuilder = tableBuilderFactory.create(singleThread);
    }

    @Test
    public void test()
    {
        doTestWithSubstring("AAES.gz", 1);
        doTestWithSubstring("AAES.gz", 5);
        doTestWithSubstring("AAES.gz", 10);
        doTestWithSubstring("AAES.gz", 20);
        doTestWithSubstring("AAES.gz", 50);
        doTestWithSubstring("AAES.gz", 100);


        doTestWithRandom("AAES.gz", 1);
        doTestWithRandom("AAES.gz", 5);
        doTestWithRandom("AAES.gz", 10);
        doTestWithRandom("AAES.gz", 20);
        doTestWithRandom("AAES.gz", 50);
        doTestWithRandom("AAES.gz", 100);


        doTestWithRandom(65000, 1);
        doTestWithRandom(65000, 5);
        doTestWithRandom(65000, 10);
        doTestWithRandom(65000, 20);
        doTestWithRandom(65000, 50);
        doTestWithRandom(65000, 100);
    }

    public void doTestWithRandom(String dnaName, int patternSize)
    {
        String text = FileHelpers.readDNA(container, dnaName);
        int length = text.length();
        String pattern = getRandomDNA(length / patternSize);
        doRun(text, pattern);
    }

    public void doTestWithRandom(int size, int patternSize)
    {
        String text = getRandomString(size);
        int length = text.length();
        String pattern = text.substring(0, length / patternSize);
        doRun(text, pattern);
    }


    private static String getRandomDNA(int length)
    {

        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i)
        {
            int index = random.nextInt(4);
            builder.append("agct".charAt(index));
        }
        return builder.toString();
    }


    private static String getRandomString(int length)
    {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i)
        {
            builder.append((char) (random.nextInt(25) + 'a'));
        }
        return builder.toString();
    }


    public void doTestWithSubstring(String dnaName, int patternSize)
    {
        String text = FileHelpers.readDNA(container, dnaName);
        int length = text.length();
        String pattern = text.substring(0, length / patternSize);
        doRun(text, pattern);

    }

    private void doRun(String text, String pattern)
    {
        SLPBuildHelper builder = container.create(SLPBuildHelper.class);

        Product[] textSlp = builder.buildSlp(container.get(ISettings.class), text);
        Product[] patternSlp = builder.buildSlp(container.get(ISettings.class), pattern);

        IAPTable apTable = tableBuilder.build(patternSlp, textSlp);
        int nonEmpty = 0;
        int total = 0;
        for (int i = 0; i < apTable.patternSize(); ++i)
        {
            for (int j = 0; j < apTable.textSize(); ++j)
            {
                if (!apTable.get(i, j).isEmpty())
                    ++nonEmpty;
                ++total;
            }
        }
        double empty = total - nonEmpty;
        System.out.format("TextSize = %s PatternSize = %s TotalCells = %s EmptyCells = %s Emptyness = %s%%\n", textSlp.length, patternSlp.length, total, total - nonEmpty, (empty / total) * 100);
    }

}

