package tests.integration.Trees;

import java.util.ArrayList;

import org.junit.Test;

import tests.integration.IntegrationTestBase;
import SLPs.SLPExtractor;
import avlTree.AvlTreeManagerFactory;
import avlTree.IAvlTreeManagerFactory;
import avlTree.buffers.AvlTreeBufferFactory;
import avlTree.mergers.AvlTreeArrayMergerFactory;
import avlTree.slpBuilders.AvlTreeSLPBuilder;
import commons.settings.ISettings;
import compressionservice.algorithms.factorization.IFactorIterator;
import compressionservice.algorithms.factorization.IFactorIteratorFactory;
import data.IDataFactory;
import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.SLPModel;
import dataContracts.statistics.Statistics;
import serialization.products.ProductsSerializer4;

public class AvlSlpBuildTest extends IntegrationTestBase {

    @Test
    public void Test()
    {
        IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(container.get(ISettings.class), DataFactoryType.file);
        AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(new AvlTreeArrayMergerFactory(), AvlMergePattern.block, AvlSplitPattern.fromFirst);
        AvlTreeSLPBuilder builder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), new ProductsSerializer4());
        SLPModel slpModel = builder.buildSlp(getFactorization("abacaba"), new Statistics());
        System.out.println(slpModel.calcStats().countRules);
    }

    private FactorDef[] getFactorization(String text) {
        IFactorIterator iterator = container.get(IFactorIteratorFactory.class).createInfiniteIterator(
                container.get(IDataFactory.class).createCharArray(text.toCharArray()), DataFactoryType.memory);
        ArrayList<FactorDef> factors = new ArrayList<FactorDef>();
        while(iterator.any())
        {
            FactorDef nextFactor = iterator.next();
            factors.add(nextFactor);
        }
        return factors.toArray(new FactorDef[0]);
    }
}
