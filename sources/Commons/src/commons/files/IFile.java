package commons.files;

import java.io.IOException;

public interface IFile extends AutoCloseable
{
    String getFileName();

    void remove();

    void appendBatch(byte[] batch, int offset, int length) throws IOException;

    void appendBatch(char[] batch, int offset, int length) throws IOException;

    int read(long offset, byte[] dst) throws IOException;

    void write(long fileOffset, byte[] batch, int offset, int length) throws IOException;

    void close();

    long size();
}
