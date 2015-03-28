package commons.files;

public interface IFileManager
{
    IFile createTempFile(String directory);

    IFile getFile(String fileName);
}
