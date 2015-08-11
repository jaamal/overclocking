package serialization.products;

import serialization.primitives.IIntArraySerializer;
import serialization.primitives.IntArraySerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dataContracts.Product;

public class TwoIntProductsSerializer implements IProductsSerializer {
    private final IIntArraySerializer intArraySerializer;

    public TwoIntProductsSerializer() {
        this(new IntArraySerializer());
    }

    public TwoIntProductsSerializer(IIntArraySerializer intArraySerializer){
        this.intArraySerializer = intArraySerializer;
    }

    @Override
    public void serialize(OutputStream stream, Product[] products) throws IOException {
        intArraySerializer.serialize(stream, makeIntegerArray(products));
    }

    @Override
    public Product[] deserialize(InputStream stream) throws IOException {
        return fromIntegerArray(intArraySerializer.deserialize(stream));
    }

    private static int[] makeIntegerArray(Product[] products) {
        int[] array = new int[products.length * 2];
        for (int i = 0; i < products.length; ++i) {
            if (products[i].isTerminal) {
                array[i * 2] = 0;
                array[i * 2 + 1] = products[i].symbol;
            } else {
                array[i * 2] = (int) products[i].first + 1;
                array[i * 2 + 1] = (int) products[i].second;
            }
        }
        return array;
    }

    private static Product[] fromIntegerArray(int[] array) {
        if (array.length % 2 != 0)
            throw new RuntimeException("Assertion failed: array length must be even!");
        Product[] products = new Product[array.length / 2];
        for (int i = 0; i < products.length; ++i) {
            if (array[2 * i] == 0)
                products[i] = new Product((char) array[2 * i + 1]);
            else
                products[i] = new Product(array[2 * i] - 1, array[2 * i + 1]);
        }
        return products;
    }
}

