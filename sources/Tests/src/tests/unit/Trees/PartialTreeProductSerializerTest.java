package tests.unit.Trees;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;
import dataContracts.Product;
import serialization.products.PartialTreeProductsSerializer;
import tests.unit.UnitTestBase;

public class PartialTreeProductSerializerTest extends UnitTestBase {

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        Product[] products = new Product[] { 
                new Product('a'), new Product('b'), new Product(0, 1), new Product(1, 1),
                new Product(1, 0), new Product(2, 3), new Product(2, 4), new Product(5, 6),
        };
        PartialTreeProductsSerializer serializer = new PartialTreeProductsSerializer();
        
        byte[] serializedProducts;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            serializer.serialize(outputStream, products);
            serializedProducts = outputStream.toByteArray();
        }
        
        Product[] actuals;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedProducts)) {
            actuals = serializer.deserialize(inputStream);
        }
        Assert.assertArrayEquals(products, actuals);
    }
}
