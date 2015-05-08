package tests.integration.Trees.avlTree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import serialization.primitives.DifferenceHeuristicIntArraySerializer;
import serialization.primitives.IntArraySerializer;
import serialization.products.IProductsSerializer;
import serialization.products.ProductsSerializer;
import serialization.products.ProductsSerializer2;
import serialization.products.ProductsSerializer3;
import tests.integration.IntegrationTestBase;
import SLPs.ProductsSerializer4;
import SLPs.SLPExtractor;
import SLPs.SlpByteSizeCounter;
import avlTree.AvlTreeManagerFactory;
import avlTree.IAvlTreeManagerFactory;
import avlTree.buffers.AvlTreeBufferFactory;
import avlTree.mergers.AvlTreeArrayMergerFactory;
import avlTree.slpBuilders.AvlTreeSLPBuilder;
import avlTree.slpBuilders.SLPBuilder;

import commons.settings.ISettings;
import compressingCore.dataAccess.IDataFactory;
import compressingCore.dataAccess.IReadableCharArray;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.algorithms.factorization.IFactorIterator;
import compressionservice.algorithms.factorization.IFactorIteratorFactory;

import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.statistics.Statistics;

public class BuildSLPFromFileTest extends IntegrationTestBase {

    @Test
    public void testBuildSLPFromFile() {
        String text = readText(Paths.get("testFiles", "simpleDNA_clean.txt"));
        SLPModel slpModel = buildSLP(text);
        Assert.assertEquals(text, slpModel.toString());
    }

    @Test
    public void testBuildSerializedSlpFromFile() throws IOException {
        String text = readText(Paths.get("testFiles", "simpleDNA_clean.txt"));
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
            Assert.assertEquals(getString(products), getString(actualProducts));
        }
        return bytes;
    }

    private String getString(Product[] products) {
        SLPBuilder builder = new SLPBuilder();
        for (int i = 0; i < products.length; i++) {
            Product product = products[i];
            long fromNumber = builder.append(product);
            Assert.assertEquals(i, fromNumber);
        }
        return builder.toSLPModel().toString();
    }

    private String readText(Path path) {
        IDataFactory dataFactory = container.get(IDataFactory.class);
        //Note! The algorithm that find lz-factorization works incorrect if input text contains spaces or special symbols.
        try (IReadableCharArray source = dataFactory.readFile(DataFactoryType.memory, path)) {
            return source.toString(0, source.length());
        }
    }

    private SLPModel buildSLP(String text) {
        try (IReadableCharArray source = new MemoryReadableCharArray(text)) {
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
            SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
            AvlTreeSLPBuilder builder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), slpByteSizeCounter);
            return builder.buildSlp(factorization.toArray(new FactorDef[0]), new Statistics());
        }
    }
}
