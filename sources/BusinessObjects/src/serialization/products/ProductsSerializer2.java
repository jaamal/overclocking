package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import commons.utils.StreamHelpers;
import dataContracts.Product;

public class ProductsSerializer2 extends AbstractProductsSerializer {

    @Override
    public void serializeProduct(OutputStream stream, int index, Product product) throws IOException {
        if (product.isTerminal) {
            stream.write(255);
            StreamHelpers.writeChar(stream, product.symbol);
        } else {
            if (product.first <= product.second) {
                long A = product.first;
                long B = product.second - product.first;
                long C = index - 1 - product.second;
                if (A <= C && B <= C)
                    encodeRule(stream, 0, A, B);
                else if (A <= B && C <= B)
                    encodeRule(stream, 1, A, C);
                else
                    encodeRule(stream, 2, B, C);
            } else {
                long A = product.second;
                long B = product.first - product.second;
                long C = index - 1 - product.first;
                if (A <= C && B <= C)
                    encodeRule(stream, 3, A, B);
                else if (A <= B && C <= B)
                    encodeRule(stream, 4, A, C);
                else
                    encodeRule(stream, 5, B, C);
            }
        }
    }
    
    private static void encodeRule(OutputStream stream, int caseNumber, long A, long B) throws IOException {
        int length1 = getLength(A);
        int length2 = getLength(B);
        int b = (caseNumber * 4 + (length1 - 1)) * 4 + (length2 - 1);
        if (b >= 255)
            throw new RuntimeException(String.format("Can not serialize SLP. b = %d must be in range [0, 254]!", b));
        stream.write(b);
        writeLong(stream, A);
        writeLong(stream, B);
    }

    @Override
    protected Product deserializeProduct(InputStream stream, int index) throws IOException {
        int b = readByte(stream);
        if (b == 255) {
            return new Product(StreamHelpers.readChar(stream));
        } else {
            int length2 = b % 4 + 1;
            b /= 4;
            int length1 = b % 4 + 1;
            b /= 4;
            int caseNumber = b;

            long A = readLong(stream, length1);
            long B = readLong(stream, length2);
            long first, second;
            switch (caseNumber) {
                case 0: {
                    first = A;
                    second = first + B;
                }
                break;
                case 1: {
                    first = A;
                    second = index - 1 - B;
                }
                break;
                case 2: {
                    second = index - 1 - B;
                    first = second - A;
                }
                break;
                case 3: {
                    second = A;
                    first = second + B;
                }
                break;
                case 4: {
                    second = A;
                    first = index - 1 - B;
                }
                break;
                case 5: {
                    first = index - 1 - B;
                    second = first - A;
                }
                break;
                default:
                    throw new RuntimeException("Assertion failed! Invalid value of caseNumber = %d");
            }
            return new Product(first, second);
        }
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
