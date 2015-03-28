package tests.unit.BusinessObjects.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import serialization.primitives.DifferenceHeuristicIntArraySerializer;
import serialization.products.IProductsSerializer;
import serialization.products.ProductsSerializer;
import serialization.products.ProductsSerializer2;
import serialization.products.ProductsSerializer3;
import serialization.products.ProductsSerializer4;
import serialization.products.SimpleProductsSerializer;
import tests.TestBase;
import dataContracts.Product;

public class SlpSerializerTest extends TestBase {
    @Override
    public void setUp() {
        super.setUp();
        simpleSlpSerializer = new SimpleProductsSerializer();
    }

    @Test
    public void Test() throws IOException {
        doTest(abrakadabra, new ProductsSerializer());
        doTest(abrakadabra, new ProductsSerializer2());
        doTest(abrakadabra, new ProductsSerializer3());
        doTest(abrakadabra, new ProductsSerializer3(new DifferenceHeuristicIntArraySerializer()));
        doTest(abrakadabra, new ProductsSerializer3(new DifferenceHeuristicIntArraySerializer(2)));
        doTest(abrakadabra, new ProductsSerializer4());
    }

    private void doTest(Product[] products, IProductsSerializer serializer) throws IOException {
        int compactBytesCount = checkSerializer(serializer, products);
        int simpleBytesCount = checkSerializer(simpleSlpSerializer, products);
        System.out.println(String.format("Compaction ratio is %f", ((double) compactBytesCount) / simpleBytesCount));
    }

    private int checkSerializer(IProductsSerializer slpSerializer, Product[] products) throws IOException {
        int bytesCount;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            slpSerializer.serialize(outputStream, products);
            byte[] bytes = outputStream.toByteArray();
            bytesCount = bytes.length;
            printInfo(bytes);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
                Product[] actualProducts = slpSerializer.deserialize(inputStream);
                checkEqualsProducts(products, actualProducts);
            }
        }
        return bytesCount;
    }

    private void printInfo(byte[] bytes) {
        System.out.println(String.format("SLP has been serialized into %d bytes", bytes.length));
        if (bytes.length <= 100) {
            for (byte b : bytes)
                System.out.print(b + " ");
            System.out.println();
        }
    }

    private void checkEqualsProducts(Product[] expectedProducts, Product[] actualProducts) {
        Assert.assertEquals(expectedProducts.length, actualProducts.length);
        for (int i = 0; i < actualProducts.length; ++i) {
            Assert.assertEquals(expectedProducts[i].isTerminal, actualProducts[i].isTerminal);
            Assert.assertEquals(expectedProducts[i].symbol, actualProducts[i].symbol);
            Assert.assertEquals(expectedProducts[i].first, actualProducts[i].first);
            Assert.assertEquals(expectedProducts[i].second, actualProducts[i].second);
        }
    }

    private SimpleProductsSerializer simpleSlpSerializer;
    public final static Product[] abrakadabra = new Product[]{
            new Product('a'), //0
            new Product('b'), //1
            new Product('r'), //2
            new Product('k'), //3
            new Product('d'), //4
            new Product(0, 1), //5 a_b
            new Product(2, 0), //6 r_a
            new Product(3, 0), //7 k_a
            new Product(7, 4), //8 ka_d
            new Product(5, 6), //9 ab_ra
            new Product(9, 8), //10 abra_kad
            new Product(10, 9)  //11 abrakad_abra
    };
}
