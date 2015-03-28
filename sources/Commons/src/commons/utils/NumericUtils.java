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

    public static int bytesToInt(byte[] buffer, int startPosition)
    {
        int result = 0;
        for (int idx = startPosition + 3; idx >= startPosition; idx--)
            result = (result << 8) | (((int) buffer[idx]) & 0xFF);
        return result;
    }


    public static void intToBytes(int value, byte[] buffer, int startPorition)
    {
        final int endPos = startPorition + 4;
        for (int idx = startPorition; idx < endPos; idx++)
        {
            buffer[idx] = (byte) (value & 0xFF);
            value = value >> 8;
        }
    }
}
