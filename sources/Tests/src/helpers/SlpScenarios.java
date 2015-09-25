package helpers;

import java.util.ArrayList;
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
import data.charArray.IReadableCharArray;
import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.SLPModel;
import dataContracts.statistics.Statistics;
import overclocking.jrobocontainer.container.IContainer;
import serialization.products.IProductSerializer;

public class SlpScenarios
{
    public static SLPModel buildSLP(String text, IContainer container) {
        try (IReadableCharArray source = container.get(IDataFactory.class).createCharArray(text.toCharArray())) {
            IFactorIteratorFactory factorIteratorFactory = container.get(IFactorIteratorFactory.class);

            ArrayList<FactorDef> factorization;
            try (IFactorIterator lzFactorIterator = factorIteratorFactory.createInfiniteIterator(source, DataFactoryType.memory)) {
                factorization = new ArrayList<FactorDef>();
                while (lzFactorIterator.any()) {
                    FactorDef factor = lzFactorIterator.next();
                    factorization.add(factor);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(container.get(ISettings.class), DataFactoryType.file);
            AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(new AvlTreeArrayMergerFactory(), AvlMergePattern.sequential, AvlSplitPattern.fromMerged);
            IProductSerializer productSerializer = container.get(IProductSerializer.class);
            AvlTreeSLPBuilder builder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), productSerializer);
            return builder.buildSlp(factorization.toArray(new FactorDef[0]), new Statistics());
        }
    }
}
