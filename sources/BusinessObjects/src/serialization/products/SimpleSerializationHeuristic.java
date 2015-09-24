package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import commons.utils.StreamHelpers;
import dataContracts.Product;

public class SimpleSerializationHeuristic extends AbstractSerializationHeuristic {
    @Override
    public void serializeProduct(OutputStream stream, int index, Product product) throws IOException {
        if (product.isTerminal) {
            stream.write(255);
            StreamHelpers.writeChar(stream, product.symbol);
        } else {
            int length1 = getLength(product.first);
            int length2 = getLength(product.second);
            int b = (length1 - 1) * 8 + (length2 - 1);
            if (b >= 254) {
                stream.write(254);
                StreamHelpers.writeLong(stream, product.first);
                StreamHelpers.writeLong(stream, product.second);
            } else {
                stream.write(b);
                writeLong(stream, product.first);
                writeLong(stream, product.second);
            }
        }
    }

    @Override
    public Product deserializeProduct(InputStream stream, int index) throws IOException {
        int b = readByte(stream);
        if (b == 255) {
            return new Product(StreamHelpers.readChar(stream));
        } else if (b == 254) {
            long first = StreamHelpers.readLong(stream);
            long second = StreamHelpers.readLong(stream);
            return new Product(first, second);
        } else {
            int length1 = b / 8 + 1;
            int length2 = b % 8 + 1;
            long first = readLong(stream, length1);
            long second = readLong(stream, length2);
            return new Product(first, second);
        }
    }
    
    @Override
    public byte getSerializerId()
    {
        return 2;
    }
    
    private static int getLength(long number) {
        int length = 0;
        do {
            ++length;
            number >>= 8;
        } while (number != 0);
        return length;
    }
    
    private static void writeLong(OutputStream stream, long number) throws IOException {
        do {
            stream.write((byte) (number & 255));
            number >>= 8;
        } while (number != 0);
    }

    private static long readLong(InputStream stream, int length) throws IOException {
        long result = 0;
        for (int i = 0; length > 0; i += 8, --length)
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
