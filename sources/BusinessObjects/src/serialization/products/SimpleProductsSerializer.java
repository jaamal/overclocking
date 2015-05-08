package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dataContracts.Product;

public class SimpleProductsSerializer extends AbstractProductsSerializer {
    @Override
    public void serializeProduct(OutputStream stream, int index, Product product) throws IOException {
        if (product.isTerminal) {
            stream.write(0);
            writeChar(stream, product.symbol);
        } else {
            stream.write(255);
            writeInt64(stream, product.first);
            writeInt64(stream, product.second);
        }
    }

    @Override
    public Product deserializeProduct(InputStream stream, int index) throws IOException {

        int b = readByte(stream);
        if (b == 0) {
            return new Product(readChar(stream));
        } else {
            long first = readLong(stream);
            long second = readLong(stream);
            return new Product(first, second);
        }
    }

    private static void writeInt64(OutputStream stream, long integer) throws IOException {
        for (int i = 0; i < 8; ++i) {
            stream.write((byte) (integer & 255));
            integer >>= 8;
        }
    }

    private static void writeChar(OutputStream stream, char character) throws IOException {
        for (int i = 0; i < 2; ++i) {
            stream.write(character & 255);
            character >>= 8;
        }
    }

    private static long readLong(InputStream stream) throws IOException {
        long result = 0;
        for (int i = 0; i < 64; i += 8)
            result |= ((long) readByte(stream)) << i;
        return result;
    }

    private static char readChar(InputStream stream) throws IOException {
        char result = 0;
        for (int i = 0; i < 16; i += 8)
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
