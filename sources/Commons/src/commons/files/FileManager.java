package commons.files;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileManager implements IFileManager
{
    @Override
    public IFile createTempFile(String workFolder)
    {
        String localFileName = UUID.randomUUID().toString();
        String fullFileName = workFolder + File.separator + localFileName;
        forceFullFilePath(fullFileName);
        return new FileImpl(fullFileName);
    }

    private static void forceFile(File file)
    {
        if (file.exists())
            return;
        try
        {
            if (!file.createNewFile())
                throw new DirectoryNotCreatedException("Problems with creation temp file: createNewFile returns false");
        }
        catch (IOException e)
        {
            throw new DirectoryNotCreatedException("Problems with creation temp file", e);
        }

    }

    private static void forceFullFilePath(String fileName)
    {
        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null)
            parent.mkdirs();
        forceFile(file);
    }

    @Override
    public IFile getFile(String fileName)
    {
        return new FileImpl(fileName);
    }
}
