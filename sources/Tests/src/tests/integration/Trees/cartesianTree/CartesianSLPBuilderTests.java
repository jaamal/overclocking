package tests.integration.Trees.cartesianTree;

import org.junit.Assert;
import org.junit.Test;
import SLPs.SLPExtractor;
import cartesianTree.CartesianTreeManagerFactory;
import cartesianTree.slpBuilders.CartesianSlpTreeBuilder;
import cartesianTree.slpBuilders.ICartesianSlpTreeBuilder;
import commons.settings.ISettings;
import dataContracts.DataFactoryType;
import dataContracts.LZFactorDef;
import dataContracts.SLPModel;
import dataContracts.statistics.Statistics;
import helpers.FactorizationScenarios;
import serialization.products.IProductSerializer;
import tests.integration.IntegrationTestBase;

public class CartesianSLPBuilderTests extends IntegrationTestBase {

    @Test
    public void test() {
        LZFactorDef[] factors = FactorizationScenarios.generate(200000);
        
        IProductSerializer productSerializer = container.get(IProductSerializer.class);
        ICartesianSlpTreeBuilder slpTreeBuilder = new CartesianSlpTreeBuilder(new CartesianTreeManagerFactory(container.get(ISettings.class), DataFactoryType.memory), new SLPExtractor(), productSerializer);
        SLPModel slpModel = slpTreeBuilder.buildSlp(factors, new Statistics());
        String expected = FactorizationScenarios.stringify(factors);
        String actuals = slpModel.toString();
        Assert.assertEquals(expected, actuals);
    }
}
