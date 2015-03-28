package compressingCore.dataAccess;

public interface ILongArray extends AutoCloseable
{
    long get(long index);
    void set(long index, long value);
    long size();
    void close();
}
