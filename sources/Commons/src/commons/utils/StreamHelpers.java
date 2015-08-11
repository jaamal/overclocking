package commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamHelpers
{
    public static int readInt(InputStream stream) throws IOException {
        int result = 0;
        for (int i = 0; i < 32; i += 8)
            result |= readByte(stream) << i;
        return result;
    }

    //TODO: non obvious method behavior
    private static int readByte(InputStream stream) throws IOException {
        int result = stream.read();
        if (result == -1)
            throw new IOException("Try to read from empty stream!");
        return result;
    }
    
    public static void writeInt(OutputStream stream, int value) throws IOException {
        for (int i = 0; i < 4; ++i) {
            stream.write(value & 255);
            value >>= 8;
        }
    }
}
