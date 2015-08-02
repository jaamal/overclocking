package commons.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

public class FileImpl implements IFile
{
    private static Logger logger = Logger.getLogger(FileImpl.class);
    private String path;
    private String name;
    private RandomAccessFile randomAccessFile;
    private FileLock fileLock;
    private FileChannel channel;

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
            throw new RuntimeException(String.format("File %s not found.", this.path), e);
        }
    }

    @Override
    public String getPathStr()
    {
        return path;
    }
    
    @Override
    public Path getPath()
    {
        return Paths.get(path);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public long size()
    {
        return new File(path).length();
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
    public MappedByteBuffer mapBuffer(long position, long size) throws IOException {
        
        if (channel == null) {
            channel = randomAccessFile.getChannel();
            fileLock = channel.tryLock();
            if (fileLock == null)
                throw new RuntimeException(String.format("Fail to take lock the file %s.", path));
            
        }
        
        return channel.map(FileChannel.MapMode.READ_WRITE, position, size);
    }

    @Override
    public void close()
    {
        try
        {
            if (fileLock != null && fileLock.isValid()) {
                fileLock.release();
                channel.close();
                fileLock = null;
                channel = null;
            }
            randomAccessFile.close();
        }
        catch (IOException e)
        {
            logger.error("Error while closing file " + path, e);
        }
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
}
