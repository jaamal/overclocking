package commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamHelpers
{
    private final static int MASK = 255;
    private final static int BITS_IN_BYTE = 8;
    private final static int BITS_IN_INT = 32;
    private final static int BITS_IN_LONG = 64;
    
    public static void writeInt(OutputStream stream, int value) throws IOException {
        for (int i = 0; i < 4; ++i) {
            stream.write(value & MASK);
            value >>= BITS_IN_BYTE;
        }
    }
    
    public static int readInt(InputStream stream) throws IOException {
        int result = 0;
        for (int i = 0; i < BITS_IN_INT; i += BITS_IN_BYTE)
            result |= readByte(stream) << i;
        return result;
    }
    
    public static void writeIntsBlock(OutputStream stream, int... values) throws IOException {
        if (values.length > 4)
            throw new RuntimeException(String.format("Fail to write block of length %s, max block size is 4.", values.length));
        
        byte[] lenghtsMask = new byte[4];
        byte[][] blockBytes = new byte[4][];
        for (int i = 0; i < values.length; ++i) {
            if (values[i] == 0) {
                blockBytes[i] = new byte[] {0};
            }
            else {
                byte[] buffer = NumericUtils.toBytes(values[i]);
                
                blockBytes[i] = null;
                for (int j = 3; j >= 0; j--) {
                    if (buffer[j] != 0 ) {
                        if (blockBytes[i] == null) 
                            blockBytes[i] = new byte[j + 1];
                    }
                    
                    if (blockBytes[i] != null) {
                        blockBytes[i][j] = buffer[j];
                    }
                }
            }
            lenghtsMask[i] = (byte) blockBytes[i].length;
        }

        stream.write(lenghtsMask);
        for (int i = 0; i < values.length; ++i)
            stream.write(blockBytes[i]);
    }
    
    public static int[] readIntsBlock(InputStream stream) throws IOException {
        int[] result = new int[4];
        byte[] lengthsMask = new byte[4];
        
        stream.read(lengthsMask);
        for (int j = 0; j < 4; ++j) {
            if (lengthsMask[j] == 0)
                break;
            
            byte[] buffer = new byte[lengthsMask[j]];
            stream.read(buffer);
            result[j] = NumericUtils.intFromBytes(buffer, 0, buffer.length);
        }
        return result;
    }
    
    public static void writeLong(OutputStream stream, long value) throws IOException {
        for (int i = 0; i < 8; ++i) {
            stream.write((byte) (value & MASK));
            value >>= BITS_IN_BYTE;
        }
    }
    
    public static long readLong(InputStream stream) throws IOException {
        long result = 0;
        for (int i = 0; i < BITS_IN_LONG; i += BITS_IN_BYTE)
            result |= ((long) readByte(stream)) << i;
        return result;
    }

    //TODO: non obvious method behavior
    private static int readByte(InputStream stream) throws IOException {
        int result = stream.read();
        if (result == -1)
            throw new IOException("Try to read from empty stream!");
        return result;
    }
}
