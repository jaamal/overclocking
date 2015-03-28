package tests.integration.Trees.avlTree;

import org.junit.Assert;
import org.junit.Test;

import tests.integration.IntegrationTestBase;
import tests.scenarios.FactorizationScanrios;
import avlTree.ConcurrentAvlTreeManagerFactory;
import avlTree.IAvlTreeManagerFactory;
import avlTree.mergers.AvlTreeArrayMergerFactory;
import avlTree.slpBuilders.ConcurrencyAvlTreeSLPBuilder;
import avlTree.slpBuilders.ConcurrentAvlBuilderStopwatches;
import avlTree.slpBuilders.ISLPBuilder;
import avlTree.treeSets.AvlTreeSetFactory;
import avlTree.treeSets.IAvlTreeSetFactory;
import dataContracts.AvlMergePattern;
import dataContracts.DataFactoryType;
import dataContracts.LZFactorDef;
import dataContracts.statistics.CompressionStatistics;

public class ConcurrencyAvlTreeSLPBuilderIntegrationTest extends IntegrationTestBase {
    @Test
    public void testOneThread() {
        container.bindInstance(IAvlTreeSetFactory.class, new AvlTreeSetFactory(new AvlTreeArrayMergerFactory().create(AvlMergePattern.recursiveBlock)));
        container.bindInstance(IAvlTreeManagerFactory.class, new ConcurrentAvlTreeManagerFactory(DataFactoryType.memory));

        System.out.println("Generating factorization.");
        LZFactorDef[] factors = FactorizationScanrios.generate(30000000);

        System.out.println("Start to build avl tree.");
        ConcurrencyAvlTreeSLPBuilder builder = container.create(ConcurrencyAvlTreeSLPBuilder.class);

        ConcurrentAvlBuilderStopwatches stopwatches = new ConcurrentAvlBuilderStopwatches();
        ISLPBuilder slpBuilder = builder.buildSlp(factors, new CompressionStatistics(), stopwatches);
        stopwatches.printTimes();

        String actual = slpBuilder.getProductString();
        String expected = FactorizationScanrios.stringify(factors);
        Assert.assertEquals(expected, actual);
    }
}
