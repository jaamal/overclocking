package tests.integration.Trees.avlTree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
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
import avlTree.slpBuilders.ISLPBuilder;
import avlTree.slpBuilders.SLPBuilder;

import commons.settings.ISettings;
import compressingCore.dataAccess.IDataFactory;
import compressingCore.dataAccess.IReadableCharArray;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressingCore.dataFiltering.FileFilter;
import compressionservice.algorithms.factorization.IFactorIterator;
import compressionservice.algorithms.factorization.IFactorIteratorFactory;

import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.files.FileType;
import dataContracts.statistics.Statistics;

public class BuildSLPFromFileTest extends IntegrationTestBase {

    @Test
    public void testBuildSLPFromFile() {
        String text = readText(Paths.get("testFiles", "simpleDNA_clean.txt"));
        ISLPBuilder slp = buildSLPFromFile(text);
        Assert.assertEquals(text, slp.getProductString());
    }

    @Test
    public void testBuildSerializedSlpFromFile() throws IOException {
//        String text = readText(Paths.get("testFiles", "simpleDNA_clean.txt"));
        String text = readTextDna(Paths.get("testFiles", "AAZK", "pubftp.pl.DlE0E0wS"));
        System.out.println(String.format("Text length is %d", text.length()));
        ISLPBuilder slp = buildSLPFromFile(text);
        Product[] products = slp.toNormalForm();
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
            long fromNumber = builder.addRule(product);
            Assert.assertEquals(i, fromNumber);
        }
        return builder.getProductString();
    }

    private String readText(Path path) {
        IDataFactory dataFactory = container.get(IDataFactory.class);
        //Note! The algorithm that find lz-factorization works incorrect if input text contains spaces or special symbols.
        try (IReadableCharArray source = dataFactory.readFile(DataFactoryType.memory, path)) {
            return getString(source);
        }
    }

    private String readTextDna(Path path) {
        FileFilter fileFilter = container.get(FileFilter.class);
        Path pathToFile = fileFilter.apply(FileType.Dna, new StringReader(readText(path)));
        return readText(pathToFile);
    }

    private ISLPBuilder buildSLPFromFile(String text) {
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
            ISLPBuilder slp = builder.buildSlp(factorization.toArray(new FactorDef[0]), new Statistics());

            Assert.assertEquals(text, slp.getProductString());
            return slp;
        }
    }

    private static String getString(IReadableCharArray source) {
        long length = source.length();
        StringBuilder stringBuilder = new StringBuilder();
        for (long i = 0; i < length; i++)
            stringBuilder.append(source.get(i));
        return stringBuilder.toString();
    }
}
