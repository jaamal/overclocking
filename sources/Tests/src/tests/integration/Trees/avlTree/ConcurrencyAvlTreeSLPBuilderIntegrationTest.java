package tests.integration.Trees.avlTree;

import helpers.FactorizationScenarios;

import org.junit.Assert;
import org.junit.Test;

import serialization.products.IProductSerializationHeuristic;
import serialization.products.PartialTreeProductsSerializer;
import tests.integration.IntegrationTestBase;
import SLPs.ConcurrentSLPExtractor;
import SLPs.ISLPExtractor;
import avlTree.ConcurrentAvlTreeManagerFactory;
import avlTree.IAvlTreeManagerFactory;
import avlTree.mergers.AvlTreeArrayMergerFactory;
import avlTree.slpBuilders.ConcurrencyAvlTreeSLPBuilder;
import avlTree.slpBuilders.IParallelExecutorFactory;
import avlTree.slpBuilders.ParallelExecutorFactory;
import avlTree.treeSets.AvlTreeSetFactory;
import avlTree.treeSets.IAvlTreeSetFactory;
import dataContracts.AvlMergePattern;
import dataContracts.DataFactoryType;
import dataContracts.LZFactorDef;
import dataContracts.SLPModel;
import dataContracts.statistics.Statistics;

public class ConcurrencyAvlTreeSLPBuilderIntegrationTest extends IntegrationTestBase {
    @Test
    public void test() {
        container.bindInstance(IAvlTreeSetFactory.class, new AvlTreeSetFactory(new AvlTreeArrayMergerFactory().create(AvlMergePattern.recursiveBlock)));
        container.bindInstance(IAvlTreeManagerFactory.class, new ConcurrentAvlTreeManagerFactory(DataFactoryType.memory));
        container.bindInstance(IParallelExecutorFactory.class, new ParallelExecutorFactory(4));
        container.bindInstance(ISLPExtractor.class, new ConcurrentSLPExtractor(4));
        container.bindInstance(IProductSerializationHeuristic.class, new PartialTreeProductsSerializer());

        System.out.println("Generating factorization.");
        LZFactorDef[] factors = FactorizationScenarios.generate(30000000);

        System.out.println("Start to build avl tree.");
        ConcurrencyAvlTreeSLPBuilder builder = container.create(ConcurrencyAvlTreeSLPBuilder.class);
        SLPModel slpModel = builder.buildSlp(factors, new Statistics());

        String actual = slpModel.toString();
        String expected = FactorizationScenarios.stringify(factors);
        Assert.assertEquals(expected, actual);
    }
}
