package compressingCore.dataAccess;

public interface IReadableCharArray extends AutoCloseable
{
    public char get(long idx);
    public long length();
    IReadableCharArray subArray(long inclusiveStartIndex, long exclusiveEndIndex);
    void close();
    
    default String toString(long start, long end)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (long i = start; i < end; i++)
            stringBuffer.append(this.get(i));
        return stringBuffer.toString();
    }
}
