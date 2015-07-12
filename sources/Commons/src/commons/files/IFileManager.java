package commons.files;

import java.io.File;

public interface IFileManager
{
    IFile createTempFile2();
    File createTempFile();
    void deleteTempFiles();
    IFile getFile(String path);
}
