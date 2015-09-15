package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import commons.utils.StreamHelpers;
import dataContracts.Product;

public class SimpleProductsSerializer extends AbstractProductsSerializer {
    @Override
    public void serializeProduct(OutputStream stream, int index, Product product) throws IOException {
        if (product.isTerminal) {
            stream.write(0);
            StreamHelpers.writeChar(stream, product.symbol);
        } else {
            stream.write(255);
            writeLong(stream, product.first);
            writeLong(stream, product.second);
        }
    }

    @Override
    public Product deserializeProduct(InputStream stream, int index) throws IOException {
        int b = readByte(stream);
        if (b == 0) {
            return new Product(StreamHelpers.readChar(stream));
        } else {
            long first = readLong(stream);
            long second = readLong(stream);
            return new Product(first, second);
        }
    }

    private static void writeLong(OutputStream stream, long value) throws IOException {
        for (int i = 0; i < 8; ++i) {
            stream.write((byte) (value & 255));
            value >>= 8;
        }
    }

    private static long readLong(InputStream stream) throws IOException {
        long result = 0;
        for (int i = 0; i < 64; i += 8)
            result |= ((long) readByte(stream)) << i;
        return result;
    }

    private static int readByte(InputStream stream) throws IOException {
        int result = stream.read();
        if (result == -1)
            throw new IOException("Try to read from empty stream!");
        return result;
    }
}
