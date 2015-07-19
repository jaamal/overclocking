package commons.files;

public interface IFileManager
{
    IFile createTempFile();
    void deleteTempFiles();
    IFile getFile(String path);
}
