package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dataContracts.Product;

public class ProductsSerializer extends IndependentProductsSerializer {
    @Override
    public void serializeOneProduct(OutputStream stream, int index, Product product) throws IOException {
        if (product.isTerminal) {
            stream.write(255);
            writeChar(stream, product.symbol);
        } else {
            int length1 = getLength(product.first);
            int length2 = getLength(product.second);
            int b = (length1 - 1) * 8 + (length2 - 1);
            if (b >= 254) {
                stream.write(254);
                writeInt64(stream, product.first);
                writeInt64(stream, product.second);
            } else {
                stream.write(b);
                writeLong(stream, product.first);
                writeLong(stream, product.second);
            }
        }
    }

    private static void writeLong(OutputStream stream, long number) throws IOException {
        do {
            stream.write((byte) (number & 255));
            number >>= 8;
        } while (number != 0);
    }

    private static int getLength(long number) {
        int length = 0;
        do {
            ++length;
            number >>= 8;
        } while (number != 0);
        return length;
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

    @Override
    public Product deserializeOneProduct(InputStream stream, int index) throws IOException {
        int b = readByte(stream);
        if (b == 255) {
            return new Product(readChar(stream));
        } else if (b == 254) {
            long first = readLong(stream, 8);
            long second = readLong(stream, 8);
            return new Product(first, second);
        } else {
            int length1 = b / 8 + 1;
            int length2 = b % 8 + 1;
            long first = readLong(stream, length1);
            long second = readLong(stream, length2);
            return new Product(first, second);
        }
    }

    private static long readLong(InputStream stream, int length) throws IOException {
        long result = 0;
        for (int i = 0; length > 0; i += 8, --length)
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
