package commons.files;

import java.io.IOException;

public interface IFile extends AutoCloseable
{
    String getPath();
    String getName();
    long size();

    int read(long offset, byte[] dst) throws IOException;
    void append(byte[] buffer) throws IOException;
    
    void delete();
    void close();
}
