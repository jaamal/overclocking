package commons.files;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

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
    public void remove()
    {
        close();
        File file = new File(path);
        if (file.exists() && file.isFile())
        {
            file.delete();
        }
    }

    @Override
    public void appendBatch(byte[] batch, int offset, int length) throws IOException
    {
        long fileLength = randomAccessFile.length();
        randomAccessFile.seek(fileLength);
        randomAccessFile.write(batch, offset, length);
    }

    @Override
    public void appendBatch(char[] batch, int offset, int length) throws IOException
    {
        //TODO encoding
        byte[] byteBatch = new byte[batch.length];
        for (int i = 0; i < batch.length; i++)
            byteBatch[i] = (byte) batch[i];
        appendBatch(byteBatch, offset, length);
    }

    @Override
    public int read(long offset, byte[] dst) throws IOException
    {
        randomAccessFile.seek(offset);
        return randomAccessFile.read(dst);
    }

    @Override
    public void write(long fileOffset, byte[] batch, int offset, int length) throws IOException
    {
        randomAccessFile.seek(fileOffset);
        randomAccessFile.write(batch, offset, length);
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
