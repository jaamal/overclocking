package tests.unit.Trees;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import avlTree.slpBuilders.InvalidProductionRuleException;
import avlTree.slpBuilders.SLPBuilder;
import dataContracts.Product;
import dataContracts.SLPStatistics;

public class SLPBuilderTests extends UnitTestBase {

    private SLPBuilder slp;

    @Override
    public void setUp() {
        super.setUp();
        slp = new SLPBuilder();
    }

    @Test(expected = InvalidProductionRuleException.class)
    public void testInvalidProductionRule() {
        slp.addRule(new Product('a'));
        slp.addRule(new Product(0, 1));
    }

    @Test
    public void testAll() {
        Product product1 = new Product('a');
        Product product2 = new Product('b');
        Product product3 = new Product(0, 1);
        Product product4 = new Product(2, 2);
        Assert.assertEquals(0, slp.addRule(product1));
        Assert.assertEquals(1, slp.addRule(product2));
        Assert.assertEquals(2, slp.addRule(product3));
        Assert.assertEquals(0, slp.addRule(product1));
        Assert.assertEquals(1, slp.addRule(product2));
        Assert.assertEquals(2, slp.addRule(product3));
        Assert.assertEquals(3, slp.addRule(product4));

        Assert.assertEquals("abab", slp.getProductString());

        SLPStatistics statistics = slp.getStatistics();
        Assert.assertEquals(4, statistics.length);
        Assert.assertEquals(4, statistics.countRules);
        Assert.assertEquals(3, statistics.height);
    }
}

