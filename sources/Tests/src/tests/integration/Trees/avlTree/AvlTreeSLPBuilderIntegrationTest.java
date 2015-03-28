package tests.integration.Trees.avlTree;

import org.junit.Assert;
import org.junit.Test;

import serialization.products.ProductsSerializer4;
import tests.integration.IntegrationTestBase;
import tests.scenarios.FactorizationScanrios;
import SLPs.SLPExtractor;
import SLPs.SlpByteSizeCounter;
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
import dataContracts.statistics.CompressionStatistics;

public class AvlTreeSLPBuilderIntegrationTest extends IntegrationTestBase {

    @Test
    public void testFileModern() {
        LZFactorDef[] factors = FactorizationScanrios.generate(200000);
        
        IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(container.get(ISettings.class), DataFactoryType.file);
        AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(new AvlTreeArrayMergerFactory(), AvlMergePattern.block, AvlSplitPattern.fromFirst);
        SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
        AvlTreeSLPBuilder builder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), slpByteSizeCounter);
        String actuals = builder.buildSlp(factors, new CompressionStatistics()).getProductString();
        String expected = FactorizationScanrios.stringify(factors);
        Assert.assertEquals(expected, actuals);
    }
}
