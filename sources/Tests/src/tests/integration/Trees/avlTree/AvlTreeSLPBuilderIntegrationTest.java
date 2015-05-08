package tests.integration.Trees.avlTree;

import org.junit.Assert;
import org.junit.Test;

import tests.integration.IntegrationTestBase;
import tests.scenarios.FactorizationScenarios;
import SLPs.ProductsSerializer4;
import SLPs.SLPExtractor;
import avlTree.AvlTreeManagerFactory;
import avlTree.IAvlTreeManagerFactory;
import avlTree.buffers.AvlTreeBufferFactory;
import avlTree.mergers.AvlTreeArrayMergerFactory;
import avlTree.slpBuilders.AvlTreeSLPBuilder;

import commons.settings.ISettings;

import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.LZFactorDef;
import dataContracts.statistics.Statistics;

public class AvlTreeSLPBuilderIntegrationTest extends IntegrationTestBase {

    @Test
    public void testFileModern() {
        LZFactorDef[] factors = FactorizationScenarios.generate(200000);
        
        IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(container.get(ISettings.class), DataFactoryType.file);
        AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(new AvlTreeArrayMergerFactory(), AvlMergePattern.block, AvlSplitPattern.fromFirst);
        AvlTreeSLPBuilder builder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), new ProductsSerializer4());
        String actuals = builder.buildSlp(factors, new Statistics()).toString();
        String expected = FactorizationScenarios.stringify(factors);
        Assert.assertEquals(expected, actuals);
    }
}
