package serialization.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IntArraySerializer implements IIntArraySerializer {

    public void serialize(OutputStream stream, int[] array) throws IOException {
        writeInt32(stream, array.length);
        for (int i = 0; i < array.length; i += 4) {
            int b = 0;
            for (int j = 0; j < 4 && i + j < array.length; ++j) {
                int length = getLength(array[i + j]);
                b |= (length - 1) << (j * 2);
            }
            stream.write(b);
            for (int j = 0; j < 4 && i + j < array.length; ++j)
                writeInt(stream, array[i + j]);
        }
    }

    private static void writeInt(OutputStream stream, int number) throws IOException {
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

    private static void writeInt32(OutputStream stream, int integer) throws IOException {
        for (int i = 0; i < 4; ++i) {
            stream.write(integer & 255);
            integer >>= 8;
        }
    }

    public int[] deserialize(InputStream stream) throws IOException {
        int count = readInt32(stream);
        int[] array = new int[count];
        for (int i = 0; i < count; i += 4) {
            int b = readByte(stream);
            for (int j = 0; j < 4 && i + j < count; ++j) {
                int length = ((b >> (2 * j)) & 3) + 1;
                array[i + j] = readInt(stream, length);
            }
        }
        return array;
    }

    private static int readInt(InputStream stream, int length) throws IOException {
        int result = 0;
        for (int i = 0; length > 0; i += 8, --length)
            result |= readByte(stream) << i;
        return result;
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

