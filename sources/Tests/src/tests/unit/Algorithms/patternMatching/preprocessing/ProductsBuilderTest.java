package tests.unit.Algorithms.patternMatching.preprocessing;

import junit.framework.Assert;

import org.junit.Test;

import patternMatching.fcpm.preprocessing.IProductFactory;
import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.preprocessing.ProductsPreprocessor;
import tests.unit.UnitTestBase;
import static org.easymock.EasyMock.expect;

public class ProductsBuilderTest extends UnitTestBase {

    private ProductsPreprocessor productsPreprocessor;
    private IProductFactory productFactory;

    @Override
    public void setUp() {
        super.setUp();
        productFactory = newMock(IProductFactory.class);
        productsPreprocessor = new ProductsPreprocessor(productFactory);
    }

    @Test
    public void testEmptySLP() {
        replayAll();
        assertArrayEquals(new Product[0], productsPreprocessor.execute(new dataContracts.Product[0]));
    }

    @Test
    public void testSimple() {
        Product terminal = new Product('a');
        expect(productFactory.create('a')).andReturn(terminal);
        Product nonTerminal = new Product(0, 0, 'a', 'a', 1, 2);
        expect(productFactory.create(0, 0, 'a', 'a', 1, 2)).andReturn(nonTerminal);
        replayAll();
        assertArrayEquals(new Product[]{terminal, nonTerminal}, productsPreprocessor.execute(new dataContracts.Product[]{new dataContracts.Product('a'), new dataContracts.Product(0, 0)}));
    }

    private static <T> void assertArrayEquals(T[] actual, T[] expected) {
        Assert.assertEquals(actual.length, expected.length);
        for (int i = 0; i < actual.length; ++i)
            Assert.assertEquals(actual[i], expected[i]);
    }
}
