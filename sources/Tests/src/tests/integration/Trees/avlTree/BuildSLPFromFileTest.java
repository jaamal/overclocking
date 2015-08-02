package tests.integration.Trees.avlTree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.junit.Test;

import SLPs.ProductsSerializer4;
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
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.statistics.Statistics;
import helpers.FactorizationScenarios;
import helpers.FileHelpers;
import junit.framework.Assert;
import serialization.primitives.DifferenceHeuristicIntArraySerializer;
import serialization.primitives.IntArraySerializer;
import serialization.products.IProductsSerializer;
import serialization.products.ProductsSerializer;
import serialization.products.ProductsSerializer2;
import serialization.products.ProductsSerializer3;
import tests.integration.IntegrationTestBase;

public class BuildSLPFromFileTest extends IntegrationTestBase {

    @Test
    public void testBuildSLPFromFile() {
        String text = FileHelpers.readTestFile("simpleDNA_clean.txt", Charset.forName("Cp1251"));
        SLPModel slpModel = buildSLP(text);
        Assert.assertEquals(text, slpModel.toString());
    }

    @Test
    public void testBuildSerializedSlpFromFile() throws IOException {
        String text = FileHelpers.readTestFile("simpleDNA_clean.txt", Charset.forName("Cp1251"));
        System.out.println(String.format("Text length is %d", text.length()));
        SLPModel slpModel = buildSLP(text);
        Assert.assertEquals(text, slpModel.toString());
        Product[] products = slpModel.toNormalForm();
        System.out.println(String.format("SLP size is %d", products.length));
        serializeSlp(text, products, new ProductsSerializer());
        serializeSlp(text, products, new ProductsSerializer2());
        serializeSlp(text, products, new ProductsSerializer3(new IntArraySerializer()));
        serializeSlp(text, products, new ProductsSerializer3(new DifferenceHeuristicIntArraySerializer()));
        serializeSlp(text, products, new ProductsSerializer3(new DifferenceHeuristicIntArraySerializer(2)));
        serializeSlp(text, products, new ProductsSerializer3(new DifferenceHeuristicIntArraySerializer(3)));
        serializeSlp(text, products, new ProductsSerializer3(new DifferenceHeuristicIntArraySerializer(4)));
        serializeSlp(text, products, new ProductsSerializer4());
        serializeSlp(text, products, new ProductsSerializer4(new DifferenceHeuristicIntArraySerializer()));
        serializeSlp(text, products, new ProductsSerializer4(new DifferenceHeuristicIntArraySerializer(2)));
        serializeSlp(text, products, new ProductsSerializer4(new DifferenceHeuristicIntArraySerializer(3)));
        serializeSlp(text, products, new ProductsSerializer4(new DifferenceHeuristicIntArraySerializer(4)));
    }

    private byte[] serializeSlp(String text, Product[] products, IProductsSerializer slpSerializer) throws IOException {
        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            slpSerializer.serialize(outputStream, products);
            bytes = outputStream.toByteArray();
        }
        System.out.println(String.format("Serialized into %d bytes. Compression ratio %f", bytes.length, bytes.length / (text.length() * 1.0)));
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            Product[] actualProducts = slpSerializer.deserialize(inputStream);
            Assert.assertEquals(FactorizationScenarios.stringify(products), FactorizationScenarios.stringify(actualProducts));
        }
        return bytes;
    }

    private SLPModel buildSLP(String text) {
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
            AvlTreeSLPBuilder builder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), new ProductsSerializer4());
            return builder.buildSlp(factorization.toArray(new FactorDef[0]), new Statistics());
        }
    }
}
