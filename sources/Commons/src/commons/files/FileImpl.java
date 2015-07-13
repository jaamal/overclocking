package commons.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

public class FileImpl implements IFile
{
    private static Logger logger = Logger.getLogger(FileImpl.class);
    private String path;
    private String name;
    private RandomAccessFile randomAccessFile;

    public FileImpl(String path)
    {
        try
        {
            File file = new File(path);
            this.path = file.getCanonicalPath();
            this.name = file.getName();
        }
        catch (IOException e)
        {
            File file = new File(path);
            this.path = file.getAbsolutePath();
            this.name = file.getName();
        }
        try
        {
            randomAccessFile = new RandomAccessFile(this.path, "rw");
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("FileNotFound: " + this.path, e);
        }
    }

    @Override
    public String getPath()
    {
        return path;
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void delete()
    {
        close();
        File file = new File(path);
        if (file.exists() && file.isFile())
        {
            file.delete();
        }
    }

    @Override
    public void append(byte[] buffer) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.length());
        randomAccessFile.write(buffer, 0, buffer.length);
    }

    @Override
    public int read(long offset, byte[] dst) throws IOException
    {
        randomAccessFile.seek(offset);
        return randomAccessFile.read(dst);
    }

    @Override
    public void close()
    {
        try
        {
            randomAccessFile.close();
        }
        catch (IOException e)
        {
            logger.error("Error while closing file " + path, e);
        }
    }

    @Override
    public long size()
    {
        return new File(path).length();
    }
}
