package tests.unit.Algorithms.patternMatching;

import org.junit.Before;
import org.junit.Test;

import patternMatching.IPatternMatcher;
import patternMatching.fcpm.FullyCompressedPatternMatcher;
import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.preprocessing.ProductFactory;
import patternMatching.fcpm.preprocessing.ProductsPreprocessor;
import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.builder.*;
import tests.unit.UnitTestBase;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FullyCompressedPatternMatcherTest extends UnitTestBase {

    private ProductsPreprocessor productsPreprocessor;
    private IAPTableBuilder apTableBuilder;
    private Product[] textSlp;
    private Product[] patternSlp;
    private IPatternMatcher matcher;

    @Before
    public void setUp() {
        super.setUp();
        productsPreprocessor = new ProductsPreprocessor(new ProductFactory());

        IPatternMatchingConfig singleThread = new IPatternMatchingConfig() {

            @Override
            public ExecutionStrategy getExecutionStrategy() {
                return ExecutionStrategy.SingleThread;
            }

            @Override
            public int getThreadCount() {
                return 1;
            }

            @Override
            public APTableType getAPTableType() {
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
    }


    private void doTest(int... occurrences) {
        Set<Integer> occurrencesSet = new HashSet<>();
        for (int i = 0; i < occurrences.length; ++i)
            occurrencesSet.add(occurrences[i]);
        if (occurrencesSet.isEmpty())
            assertFalse(matcher.contains());
        else
            assertTrue(matcher.contains());

        Product lastTextProduct = textSlp[textSlp.length - 1];
        for (int i = 0; i < lastTextProduct.Length; ++i)
            if (occurrencesSet.contains(i))
                assertTrue(matcher.contains(i));
            else
                assertFalse(matcher.contains(i));
        assertEquals(occurrencesSet.size(), matcher.count());
    }

    @Test
    public void TestAbraInAbrakadabra() {
        init(AbrakadabraHelpers.abrakadabra, AbrakadabraHelpers.abra);
        doTest(0, 7);
    }

    @Test
    public void TestAInAbrakadabra() {
        init(AbrakadabraHelpers.abrakadabra, AbrakadabraHelpers.a);
        doTest(0, 3, 5, 7, 10);
    }

    @Test
    public void TestCNotInAbrakadabra() {
        init(AbrakadabraHelpers.abrakadabra, AbrakadabraHelpers.c);
        doTest();
    }


    @Test
    public void TestAInA() {
        init(AbrakadabraHelpers.a, AbrakadabraHelpers.a);
        doTest(0);
    }

    @Test
    public void TestCNotInA() {
        init(AbrakadabraHelpers.a, AbrakadabraHelpers.c);
        doTest();
    }

    @Test
    public void TestBANotInAbrakadabra() {
        init(AbrakadabraHelpers.abrakadabra, AbrakadabraHelpers.ba);
        doTest();
    }


    @Test
    public void TestAbrakadabraInAbrakadabra() {
        init(AbrakadabraHelpers.abrakadabra, AbrakadabraHelpers.abrakadabra);
        doTest(0);
    }

    private void init(dataContracts.Product[] text, dataContracts.Product[] pattern) {
        textSlp = productsPreprocessor.execute(text);
        patternSlp = productsPreprocessor.execute(pattern);
        IAPTable table = apTableBuilder.build(patternSlp, textSlp);
        matcher = new FullyCompressedPatternMatcher(patternSlp, textSlp, table);
    }
}
