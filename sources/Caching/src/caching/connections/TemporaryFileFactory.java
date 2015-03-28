package caching.connections;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import commons.settings.ISettings;
import commons.settings.KnownKeys;

public class TemporaryFileFactory implements ITemporaryFileFactory
{
    private Path workingDir;

    public TemporaryFileFactory(ISettings settings)
    {
        this.workingDir = settings.getPath(KnownKeys.ServerWorkingDir);
    }

    @Override
    public File getTemporaryFile()
    {
        try
        {
            Files.createDirectories(workingDir);
            return Files.createTempFile(workingDir, "overclocking", "temp").toFile();
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Fail to create temporary file at dir %s.", workingDir.toString()), e);
        }
    }
}
