package compressingCore.dataAccess;

public interface ILongArray extends AutoCloseable
{
    long get(long index);
    void set(long index, long value);
    default void set(long fromIndex, long[] values) {
        for (int i = 0; i < values.length; i++) {
            set(fromIndex + i, values[i]);
        }
    }
    
    long size();
    void close();
}
