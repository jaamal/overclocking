package commons.files;

import java.io.IOException;
import java.nio.file.Path;

public interface IFile extends AutoCloseable
{
    String getPathStr();
    Path getPath();
    String getName();
    long size();

    int read(long offset, byte[] dst) throws IOException;
    void append(byte[] buffer) throws IOException;
    
    void delete();
    void close();
}
