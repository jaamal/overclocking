package tests.unit.Algorithms.patternMatching.localsearch;

import org.junit.Test;

import patternMatching.fcpm.PatternMatchingContext;
import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.localsearch.ILocalSearchResult;
import patternMatching.fcpm.localsearch.LocalSearchResult;
import patternMatching.fcpm.localsearch.LocalSearchResultFactory;
import patternMatching.fcpm.localsearch.RecursiveLocalSearchExecutor;
import patternMatching.fcpm.preprocessing.IProductsPreprocessor;
import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.preprocessing.ProductFactory;
import patternMatching.fcpm.preprocessing.ProductsPreprocessor;
import patternMatching.fcpm.table.IAPTable;
import tests.unit.UnitTestBase;
import tests.unit.Algorithms.patternMatching.AbrakadabraHelpers;
import tests.unit.Algorithms.patternMatching.arithmeticProgression.table.StupidAPTableBuilder;
import static junit.framework.Assert.assertEquals;

public class RecursiveLocalSearchExecutorTests extends UnitTestBase
{

    private IProductsPreprocessor preprocessor = new ProductsPreprocessor(new ProductFactory());
    private RecursiveLocalSearchExecutor localSearchExecutor;
    private IAPTable apTable;
    private PatternMatchingContext context;

    private Product[] preproccess(dataContracts.Product[] products)
    {
        return preprocessor.execute(products);
    }

    private void setUp(dataContracts.Product[] pattern, dataContracts.Product[] text)
    {
        StupidAPTableBuilder stupidAPTableBuilder = new StupidAPTableBuilder();
        Product[] preproccessPattern = preproccess(pattern);
        Product[] preproccessText = preproccess(text);
        apTable = stupidAPTableBuilder.build(preproccessPattern, preproccessText);
        localSearchExecutor = new RecursiveLocalSearchExecutor(new LocalSearchResultFactory());
        context = new PatternMatchingContext(apTable, preproccessPattern, preproccessText);
    }

    @Test
    public void test()
    {
        setUp(AbrakadabraHelpers.abra, AbrakadabraHelpers.abrakadabra);
        //abra in abrakadabra
        assertEquals(searchResult(ap(0, 7, 2)), find(5, 11, 0, 10));
        assertEquals(searchResult(ap(0)), find(5, 11, 0, 9));
        assertEquals(searchResult(ap(7)), find(5, 11, 1, 10));
        assertEquals(searchResult(), find(5, 11, 1, 9));

        //a in abrakadabra
        assertEquals(searchResult(ap(5, 2, 2)), find(0, 11, 5, 7));
        assertEquals(searchResult(ap(7)), find(0, 11, 6, 7));
        assertEquals(searchResult(ap(5)), find(0, 11, 5, 6));

        //ab in abrakadabra
        assertEquals(searchResult(), find(3, 11, 5, 7));
        assertEquals(searchResult(ap(7)), find(3, 11, 5, 8));


        //ra in abrakadabra
        assertEquals(searchResult(ap(2)), find(4, 11, 2, 7));
        assertEquals(searchResult(), find(4, 11, 3, 7));

    }

    private ILocalSearchResult find(int patternIndex, int textIndex, int a, int b)
    {
        return localSearchExecutor.find(context, patternIndex, textIndex, a, b);
    }

    private static ArithmeticProgression ap(int element)
    {
        return ArithmeticProgression.create(element);
    }

    private static ArithmeticProgression ap(int element, int difference, int count)
    {
        return ArithmeticProgression.create(element, difference, count);
    }


    private static ILocalSearchResult searchResult(ArithmeticProgression... progressions)
    {
        LocalSearchResult localSearchResult = new LocalSearchResult();
        for (ArithmeticProgression ap : progressions)
            localSearchResult.add(ap);
        return localSearchResult;
    }
}
