package commons.files;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileManager implements IFileManager
{
    @Override
    public IFile createTempFile(String workFolder)
    {
        String filePath = workFolder + File.separator + UUID.randomUUID().toString();
        tryCreateFile(new File(filePath));
        return new FileImpl(filePath);
    }

    @Override
    public IFile getFile(String fileName)
    {
        return new FileImpl(fileName);
    }
    
    private static void tryCreateFile(File file)
    {
        File parent = file.getParentFile();
        if (parent != null)
            parent.mkdirs();
        
        if (file.exists())
            return;
        try
        {
            if (!file.createNewFile())
                throw new RuntimeException(String.format("Fail to create new file %s, since createNewFile returns false", file.getPath()));
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Fail to create new file %s.", file.getPath()), e);
        }
    }
}
