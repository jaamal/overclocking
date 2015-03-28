package serialization.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BooleanArraySerializer implements IBooleanArraySerializer {
    @Override
    public void serialize(OutputStream stream, boolean[] array) throws IOException {
        writeInt32(stream, array.length);
        for (int i = 0; i < array.length; i += 8) {
            int b = 0;
            for (int j = 0; j < 8 && i + j < array.length; ++j)
                b |= (array[i + j] ? 1 : 0) << j;
            stream.write(b);
        }
    }

    @Override
    public boolean[] deserialize(InputStream stream) throws IOException {
        int length = readInt32(stream);
        boolean[] array = new boolean[length];
        for (int i = 0; i < array.length; i += 8) {
            int b = readByte(stream);
            for (int j = 0; j < 8 && i + j < array.length; ++j)
                array[i + j] = (b & (1 << j)) != 0;
        }
        return array;
    }

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
