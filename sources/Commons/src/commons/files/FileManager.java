package commons.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import commons.settings.ISettings;
import commons.settings.KnownKeys;

public class FileManager implements IFileManager
{
    private Path workDirPath;

    public FileManager(ISettings settings)
    {
        if (settings != null)
            workDirPath = settings.getPath(KnownKeys.ServerWorkingDir);
        else {
            workDirPath = Paths.get(System.getProperty("user.dir"), "tmp");
        }
    }
    
    @Override
    public IFile createTempFile2()
    {
        File tempFile = createTempFile();
        return new FileImpl(tempFile.getPath());
    }
    
    @Override
    public File createTempFile() {
        try
        {
            Files.createDirectories(workDirPath);
            return Files.createTempFile(workDirPath, "ov", ".tmp").toFile();
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Fail to create temporary file at dir %s.", workDirPath), e);
        }
    }
    
    @Override
    public void deleteTempFiles() {
        try {
            Files.walkFileTree(workDirPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(String.format("Fail to delete all temp files by path %s.", workDirPath), e);
        }
    }

    @Override
    public IFile getFile(String path)
    {
        return new FileImpl(path);
    }
}
