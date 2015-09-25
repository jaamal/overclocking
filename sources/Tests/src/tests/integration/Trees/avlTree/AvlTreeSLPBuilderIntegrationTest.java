package tests.integration.Trees.avlTree;

import org.junit.Assert;
import org.junit.Test;
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
import helpers.FactorizationScenarios;
import serialization.products.IProductSerializer;
import tests.integration.IntegrationTestBase;

public class AvlTreeSLPBuilderIntegrationTest extends IntegrationTestBase {

    @Test
    public void testFileModern() {
        LZFactorDef[] factors = FactorizationScenarios.generate(200000);
        
        IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(container.get(ISettings.class), DataFactoryType.file);
        AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(new AvlTreeArrayMergerFactory(), AvlMergePattern.block, AvlSplitPattern.fromFirst);
        IProductSerializer productSerializer = container.get(IProductSerializer.class);
        AvlTreeSLPBuilder builder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), productSerializer);
        String actuals = builder.buildSlp(factors, new Statistics()).toString();
        String expected = FactorizationScenarios.stringify(factors);
        Assert.assertEquals(expected, actuals);
    }
}
