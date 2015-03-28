package compressingCore.dataAccess;

public class MemoryReadableCharArray implements IReadableCharArray
{
    private char[] charArray;
    private int startIndex;
    private int length;

    public MemoryReadableCharArray(char[] charArray, int startIndex, int length)
    {
        this.charArray = charArray;
        this.startIndex = startIndex;
        this.length = length;
    }

    public MemoryReadableCharArray(String string)
    {
        this(string.toCharArray(), 0, string.length());
    }

    @Override
    public char get(long idx)
    {
        return charArray[(int) idx + startIndex];
    }

    @Override
    public long length()
    {
        return length;
    }

    @Override
    public IReadableCharArray subArray(long inclusiveStartIndex, long exclusiveEndIndex)
    {
        return new MemoryReadableCharArray(charArray, (int) (inclusiveStartIndex + startIndex), (int) (exclusiveEndIndex - inclusiveStartIndex));
    }

    @Override
    public void close()
    {
    }
}
