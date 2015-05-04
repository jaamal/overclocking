package commons.files;

import java.io.IOException;

public interface IFile extends AutoCloseable
{
    String getPath();
    String getName();
    long size();

    int read(long offset, byte[] dst) throws IOException;

    void write(long fileOffset, byte[] batch, int offset, int length) throws IOException;
    void appendBatch(byte[] batch, int offset, int length) throws IOException;
    void appendBatch(char[] batch, int offset, int length) throws IOException;
    
    void remove();
    void close();
}
