package tests.integration.Trees.cartesianTree;

import org.junit.Assert;
import org.junit.Test;

import serialization.products.ProductsSerializer4;
import tests.integration.IntegrationTestBase;
import tests.scenarios.FactorizationScanrios;
import SLPs.SLPExtractor;
import SLPs.SlpByteSizeCounter;
import avlTree.slpBuilders.ISLPBuilder;
import cartesianTree.CartesianTreeManagerFactory;
import cartesianTree.slpBuilders.CartesianSlpTreeBuilder;
import cartesianTree.slpBuilders.ICartesianSlpTreeBuilder;

import commons.settings.ISettings;

import dataContracts.DataFactoryType;
import dataContracts.LZFactorDef;
import dataContracts.statistics.Statistics;

public class CartesianSLPBuilderTests extends IntegrationTestBase {

    @Test
    public void integrationTest() {
        LZFactorDef[] factors = FactorizationScanrios.generate(200000);
        
        SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
        ICartesianSlpTreeBuilder slpTreeBuilder = new CartesianSlpTreeBuilder(new CartesianTreeManagerFactory(container.get(ISettings.class), DataFactoryType.memory), new SLPExtractor(), slpByteSizeCounter);
        ISLPBuilder slp = slpTreeBuilder.buildSlp(factors, new Statistics());
        String expected = FactorizationScanrios.stringify(factors);
        Assert.assertEquals(expected, slp.getProductString());
    }
}
