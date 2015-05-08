package tests.unit.Trees;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import avlTree.slpBuilders.ConcurrentSLPBuilder;
import avlTree.slpBuilders.InvalidProductionRuleException;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.SLPStatistics;

public class ConcurrentSLPBuilderTests extends UnitTestBase {

    private ConcurrentSLPBuilder slpBuilder;

    @Override
    public void setUp() {
        super.setUp();
        slpBuilder = new ConcurrentSLPBuilder();
    }

    @Test(expected = InvalidProductionRuleException.class)
    public void testInvalidProductionRule() {
        slpBuilder.append(new Product('a'));
        slpBuilder.append(new Product(0, 1));
    }

    @Test
    public void testAll() {
        Product product1 = new Product('a');
        Product product2 = new Product('b');
        Product product3 = new Product(0, 1);
        Product product4 = new Product(2, 2);
        Assert.assertEquals(0, slpBuilder.append(product1));
        Assert.assertEquals(1, slpBuilder.append(product2));
        Assert.assertEquals(2, slpBuilder.append(product3));
        Assert.assertEquals(0, slpBuilder.append(product1));
        Assert.assertEquals(1, slpBuilder.append(product2));
        Assert.assertEquals(2, slpBuilder.append(product3));
        Assert.assertEquals(3, slpBuilder.append(product4));

        SLPModel slpModel = slpBuilder.toSLPModel();
        Assert.assertEquals("abab", slpModel.toString());
        SLPStatistics statistics = slpModel.calcStats();
        Assert.assertEquals(4, statistics.length);
        Assert.assertEquals(4, statistics.countRules);
        Assert.assertEquals(3, statistics.height);
    }
}
