package tests.unit.BusinessObjects;

import org.junit.Test;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.SLPStatistics;
import junit.framework.Assert;
import productEnumerator.ProductEnumerator;
import tests.unit.UnitTestBase;

public class ProductEnumeratorTests extends UnitTestBase {

    private ProductEnumerator productEnumerator;

    @Override
    public void setUp() {
        super.setUp();
        productEnumerator = new ProductEnumerator();
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidProductionRule() {
        productEnumerator.append(new Product('a'));
        productEnumerator.append(new Product(0, 1));
    }

    @Test
    public void testAll() {
        Product product1 = new Product('a');
        Product product2 = new Product('b');
        Product product3 = new Product(0, 1);
        Product product4 = new Product(2, 2);
        Assert.assertEquals(0, productEnumerator.append(product1));
        Assert.assertEquals(1, productEnumerator.append(product2));
        Assert.assertEquals(2, productEnumerator.append(product3));
        Assert.assertEquals(0, productEnumerator.append(product1));
        Assert.assertEquals(1, productEnumerator.append(product2));
        Assert.assertEquals(2, productEnumerator.append(product3));
        Assert.assertEquals(3, productEnumerator.append(product4));

        SLPModel slpModel = productEnumerator.toSLPModel();
        Assert.assertEquals("abab", slpModel.toString());
        SLPStatistics statistics = slpModel.calcStats();
        Assert.assertEquals(4, statistics.length);
        Assert.assertEquals(4, statistics.countRules);
        Assert.assertEquals(3, statistics.height);
    }
}

