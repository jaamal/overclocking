package compressingCore.dataAccess;

public interface IReadableCharArray extends AutoCloseable
{
    public char get(long idx);

    public long length();

    IReadableCharArray subArray(long inclusiveStartIndex, long exclusiveEndIndex);

    void close();
}
