package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dataContracts.Product;

public abstract class IndependentProductsSerializer implements IProductsSerializer {
    @Override
    public void serialize(OutputStream stream, Product[] products) throws IOException {
        writeInt32(stream, products.length);
        for (int i = 0; i < products.length; i++)
            serializeOneProduct(stream, i, products[i]);
    }

    @Override
    public Product[] deserialize(InputStream stream) throws IOException {
        int productsCount = readInt32(stream);
        Product[] products = new Product[productsCount];
        for (int i = 0; i < productsCount; ++i)
            products[i] = deserializeOneProduct(stream, i);
        return products;
    }

    protected abstract void serializeOneProduct(OutputStream stream, int index, Product product) throws IOException;

    protected abstract Product deserializeOneProduct(InputStream stream, int index) throws IOException;


    private static void writeInt32(OutputStream stream, int integer) throws IOException {
        for (int i = 0; i < 4; ++i) {
            stream.write(integer & 255);
            integer >>= 8;
        }
    }

    private static int readInt32(InputStream stream) throws IOException {
        int result = 0;
        for (int i = 0; i < 32; i += 8)
            result |= readByte(stream) << i;
        return result;
    }

    private static int readByte(InputStream stream) throws IOException {
        int result = stream.read();
        if (result == -1)
            throw new IOException("Try to read from empty stream!");
        return result;
    }
}
