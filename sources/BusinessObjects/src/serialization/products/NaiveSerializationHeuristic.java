package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import commons.utils.StreamHelpers;
import dataContracts.Product;

public class NaiveSerializationHeuristic extends AbstractSerializationHeuristic {
    @Override
    public void serializeProduct(OutputStream stream, int index, Product product) throws IOException {
        if (product.isTerminal) {
            stream.write(0);
            StreamHelpers.writeChar(stream, product.symbol);
        } else {
            stream.write(255);
            StreamHelpers.writeLong(stream, product.first);
            StreamHelpers.writeLong(stream, product.second);
        }
    }

    @Override
    public Product deserializeProduct(InputStream stream, int index) throws IOException {
        int b = readByte(stream);
        if (b == 0) {
            return new Product(StreamHelpers.readChar(stream));
        } else {
            long first = StreamHelpers.readLong(stream);
            long second = StreamHelpers.readLong(stream);
            return new Product(first, second);
        }
    }

    private static int readByte(InputStream stream) throws IOException {
        int result = stream.read();
        if (result == -1)
            throw new IOException("Try to read from empty stream!");
        return result;
    }

    @Override
    public byte getSerializerId()
    {
        return 1;
    }
}
