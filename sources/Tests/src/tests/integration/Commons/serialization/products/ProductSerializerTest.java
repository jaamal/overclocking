package tests.integration.Commons.serialization.products;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import org.junit.Test;
import dataContracts.Product;
import dataContracts.SLPModel;
import helpers.FactorizationScenarios;
import helpers.FileHelpers;
import helpers.SlpScenarios;
import junit.framework.Assert;
import serialization.primitives.DifferenceHeuristicIntArraySerializer;
import serialization.products.DifferenceSerializationHeuristic;
import serialization.products.IProductSerializationHeuristic;
import serialization.products.IProductSerializer;
import serialization.products.NaiveSerializationHeuristic;
import serialization.products.PartialTreeProductsSerializer;
import serialization.products.SimpleSerializationHeuristic;
import tests.integration.IntegrationTestBase;

public class ProductSerializerTest extends IntegrationTestBase
{
    @Test
    public void testProductSerializer() throws IOException {
        String text = FileHelpers.readTestFile("simpleDNA_clean.txt", Charset.forName("Cp1251"));
        System.out.println(String.format("Text length is %d", text.length()));
        Product[] products = SlpScenarios.buildSLP(text, container).toNormalForm();
        System.out.println(String.format("SLP size is %d", products.length));
        
        IProductSerializer productSerializer = container.get(IProductSerializer.class);
        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            productSerializer.serialize(outputStream, products);
            bytes = outputStream.toByteArray();
        }
        System.out.println(String.format("Serialized into %d bytes. Compression ratio %f", bytes.length, bytes.length / (text.length() * 1.0)));
    }
    
    @Test
    public void testProductSerializationHeuristics() throws IOException {
        String text = FileHelpers.readTestFile("simpleDNA_clean.txt", Charset.forName("Cp1251"));
        System.out.println(String.format("Text length is %d", text.length()));
        SLPModel slpModel = SlpScenarios.buildSLP(text, container);
        Assert.assertEquals(text, slpModel.toString());
        Product[] products = slpModel.toNormalForm();
        System.out.println(String.format("SLP size is %d", products.length));
        serializeSlp(text, products, new NaiveSerializationHeuristic());
        serializeSlp(text, products, new SimpleSerializationHeuristic());
        serializeSlp(text, products, new DifferenceSerializationHeuristic());
        serializeSlp(text, products, new PartialTreeProductsSerializer());
        serializeSlp(text, products, PartialTreeProductsSerializer.create(new DifferenceHeuristicIntArraySerializer()));
        serializeSlp(text, products, PartialTreeProductsSerializer.create(new DifferenceHeuristicIntArraySerializer(2)));
        serializeSlp(text, products, PartialTreeProductsSerializer.create(new DifferenceHeuristicIntArraySerializer(3)));
        serializeSlp(text, products, PartialTreeProductsSerializer.create(new DifferenceHeuristicIntArraySerializer(4)));
    }

    private byte[] serializeSlp(String text, Product[] products, IProductSerializationHeuristic slpSerializer) throws IOException {
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
}
