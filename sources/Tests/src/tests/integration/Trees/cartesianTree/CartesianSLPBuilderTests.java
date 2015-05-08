package tests.integration.Trees.cartesianTree;

import org.junit.Assert;
import org.junit.Test;

import tests.integration.IntegrationTestBase;
import tests.scenarios.FactorizationScenarios;
import SLPs.ProductsSerializer4;
import SLPs.SLPExtractor;
import SLPs.SlpByteSizeCounter;
import cartesianTree.CartesianTreeManagerFactory;
import cartesianTree.slpBuilders.CartesianSlpTreeBuilder;
import cartesianTree.slpBuilders.ICartesianSlpTreeBuilder;

import commons.settings.ISettings;

import dataContracts.DataFactoryType;
import dataContracts.LZFactorDef;
import dataContracts.SLPModel;
import dataContracts.statistics.Statistics;

public class CartesianSLPBuilderTests extends IntegrationTestBase {

    @Test
    public void test() {
        LZFactorDef[] factors = FactorizationScenarios.generate(200000);
        
        SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
        ICartesianSlpTreeBuilder slpTreeBuilder = new CartesianSlpTreeBuilder(new CartesianTreeManagerFactory(container.get(ISettings.class), DataFactoryType.memory), new SLPExtractor(), slpByteSizeCounter);
        SLPModel slpModel = slpTreeBuilder.buildSlp(factors, new Statistics());
        String expected = FactorizationScenarios.stringify(factors);
        String actuals = slpModel.toString();
        Assert.assertEquals(expected, actuals);
    }
}
