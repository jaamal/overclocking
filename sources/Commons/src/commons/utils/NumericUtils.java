package commons.utils;

public class NumericUtils
{
    public static long bytesToLong(byte[] buffer, int startPosition)
    {
        long lResult = 0;
        int iLength = 8;
        for (int idx = startPosition + iLength - 1; idx >= startPosition; idx--)
        {
            lResult = (lResult << 8) | (((int) buffer[idx]) & 0xFF);
        }
        return lResult;
    }

    public static void longToBytes(long value, byte[] buffer, int bufferStartPosition)
    {
        int endPos = bufferStartPosition + 8;
        for (int idx = bufferStartPosition; idx < endPos; idx++)
        {
            buffer[idx] = (byte) (value & 0xFF);
            value = value >> 8;
        }
    }

    public static int intFromBytes(byte[] buffer)
    {
        return intFromBytes(buffer, 0, 4);
    }
    
    public static int intFromBytes(byte[] buffer, int startPosition, int length)
    {
        int result = 0;
        for (int idx = startPosition + length - 1; idx >= startPosition; idx--)
            result = (result << 8) | (((int) buffer[idx]) & 0xFF);
        return result;
    }

    public static byte[] toBytes(int value)
    {
        byte[] result = new byte[4];
        for (int idx = 0; idx < 4; idx++)
        {
            result[idx] = (byte) (value & 0xFF);
            value = value >> 8;
        }
        return result;
    }
    
    public static int intFromFloatingBytes(byte[] buffer)
    {
        return intFromBytes(buffer, 0, buffer.length);
    }
    
    //TODO: how to make it faster
    public static byte[] toFloatingBytes(int value)
    {
        if (value == 0)
            return new byte[] {0};
        
        byte[] buffer = toBytes(value);
        int resultLength = 1;
        for (int i = 1; i < 4; i++) {
            if (buffer[i] != 0)
                resultLength ++;
        }
        byte[] result = new byte[resultLength];
        System.arraycopy(buffer, 0, result, 0, resultLength);
        return result;
    }
}
