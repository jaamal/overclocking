package tests.unit.Algorithms.patternMatching.arithmeticProgression.table;

import junit.framework.Assert;

import org.junit.Test;

import dataContracts.Product;
import patternMatching.fcpm.preprocessing.ProductFactory;
import patternMatching.fcpm.preprocessing.ProductsPreprocessor;
import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.builder.*;
import tests.unit.UnitTestBase;
import static tests.unit.Algorithms.patternMatching.AbrakadabraHelpers.*;

public class APTableBuilderTest extends UnitTestBase
{
    private IAPTableBuilder apTableBuilder;
    private ProductsPreprocessor ProductsPreprocessor;
    private StupidAPTableBuilder stupidAPTableBuilder;

    @Override
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

        apTableBuilder = tableBuilderFactory.create(singleThread);

        ProductsPreprocessor = new ProductsPreprocessor(new ProductFactory());
        stupidAPTableBuilder = new StupidAPTableBuilder();
    }

    @Test
    public void test()
    {
        doTest(a, abra);
        doTest(abra, abra);
        doTest(abra, abrakadabra);
        doTest(abr, abrakadabra);
        doTest(abrakadabra, abrakadabra);
        doTest(a1ba, abaccc);
        doTest(ccc, abaccc);
        doTest(aa, ab1a);
        doTest(a1aa, aaa1aa);
    }

    private void doTest(Product[] pattern, Product[] text)
    {
        patternMatching.fcpm.preprocessing.Product[] patternSLP = ProductsPreprocessor.execute(pattern);
        patternMatching.fcpm.preprocessing.Product[] textSLP = ProductsPreprocessor.execute(text);
        IAPTable actualTable = apTableBuilder.build(patternSLP, textSLP);
        IAPTable expectedTable = stupidAPTableBuilder.build(patternSLP, textSLP);
        expectedEquals(expectedTable, actualTable, patternSLP, textSLP);
    }


    private static void expectedEquals(IAPTable expectedTable, IAPTable actualTable, patternMatching.fcpm.preprocessing.Product[] patternSLP, patternMatching.fcpm.preprocessing.Product[] textSLP)
    {
        Assert.assertEquals(expectedTable.patternSize(), actualTable.patternSize());
        Assert.assertEquals(expectedTable.textSize(), actualTable.textSize());

        for (int i = 0; i < expectedTable.patternSize(); ++i)
        {
            for (int j = 0; j < expectedTable.textSize(); ++j)
            {
                Assert.assertEquals(String.format("i = %d(%s), j = %d(%s)", i, SLPHelper.getText(patternSLP, i), j, SLPHelper.getText(textSLP, j)),
                        expectedTable.get(i, j), actualTable.get(i, j)
                );
            }
        }
    }
}

