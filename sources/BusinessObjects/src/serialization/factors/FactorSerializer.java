package serialization.factors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import commons.utils.StreamHelpers;
import dataContracts.FactorDef;
import serialization.products.LengthCalculatorOutputStream;

public class FactorSerializer implements IFactorSerializer
{
    @Override
    public void serialize(OutputStream stream, FactorDef[] factors) throws IOException
    {
        StreamHelpers.writeInt(stream, factors.length);
        for (int i = 0; i < factors.length; i++)
            serializeFactor(stream, factors[i]);
    }

    @Override
    public FactorDef[] deserialize(InputStream stream) throws IOException
    {
        int count = StreamHelpers.readInt(stream);
        FactorDef[] result = new FactorDef[count];
        for (int i = 0; i < count; ++i)
            result[i] = deserializeFactor(stream);
        return result;
    }
    
    @Override
    public long calcSizeInBytes(Iterable<FactorDef> factors)
    {
        try (LengthCalculatorOutputStream fakeStream = new LengthCalculatorOutputStream()) {
            for (FactorDef factor : factors) {
                serializeFactor(fakeStream, factor);
            }
            return fakeStream.getLengthInBytes();
        }
        catch (IOException e) {
            throw new RuntimeException("Fail to calculate serialized size.", e);
        }
    }
    
    private void serializeFactor(OutputStream stream, FactorDef product) throws IOException {
        if (product.isTerminal) {
            stream.write(255);
            StreamHelpers.writeChar(stream, (char) product.symbol);
        } else {
            int length1 = getLength(product.beginPosition);
            int length2 = getLength(product.length);
            int b = (length1 - 1) * 8 + (length2 - 1);
            if (b >= 254) {
                stream.write(254);
                StreamHelpers.writeLong(stream, product.beginPosition);
                StreamHelpers.writeLong(stream, product.length);
            } else {
                stream.write(b);
                writeLong(stream, product.beginPosition);
                writeLong(stream, product.length);
            }
        }
    }

    private FactorDef deserializeFactor(InputStream stream) throws IOException {
        int b = readByte(stream);
        if (b == 255) {
            return new FactorDef(StreamHelpers.readChar(stream));
        } else if (b == 254) {
            long beginPosition = StreamHelpers.readLong(stream);
            long length = StreamHelpers.readLong(stream);
            return new FactorDef(beginPosition, length);
        } else {
            int length1 = b / 8 + 1;
            int length2 = b % 8 + 1;
            long beginPosition = readLong(stream, length1);
            long length = readLong(stream, length2);
            return new FactorDef(beginPosition, length);
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
