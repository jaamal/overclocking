package data.enumerableData;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;

import caching.connections.ConnectionNotOpenedYetException;
import caching.connections.FileConnectionIOException;

import commons.settings.ISettings;
import commons.settings.KnownKeys;

//TODO: this class should be at connections
public class MemoryMappedFileConnection<T>
{
    private final File file;
    private FileLock fileLock;
    private ArrayList<MappedByteBuffer> mappedByteBuffers;
    private FileChannel channel;
    private int batchSize;
    private boolean deleteOnClose;
    private RandomAccessFile randomAccessFile;
    
    private int objectLength;
    private IItemSerializer<T> serializer;

    public MemoryMappedFileConnection(
            File file,
            ISettings settings,
            IItemSerializer<T> serializer,
            boolean deleteOnClose)
    {
        this.serializer = serializer;
        objectLength = serializer.itemSizeInBytes();
        this.deleteOnClose = deleteOnClose;
        this.batchSize = settings.getInt(KnownKeys.MemoryMappedFileBatchSize);
        this.file = file;
        this.fileLock = null;
        this.mappedByteBuffers = new ArrayList<MappedByteBuffer>();
    }

    public void close()
    {
        try
        {
            for (MappedByteBuffer byteBuffer : mappedByteBuffers)
                byteBuffer.force();
            if (randomAccessFile != null)
                randomAccessFile.close();
            if (deleteOnClose)
                file.delete();
        }
        catch (IOException e)
        {
            throw new FileConnectionIOException(file, e);
        }
        finally
        {
            mappedByteBuffers = new ArrayList<MappedByteBuffer>();
            channel = null;
            fileLock = null;
        }
    }

    public void open()
    {
        if (channel != null)
            return;
        try
        {
            randomAccessFile = new RandomAccessFile(file, "rw");
            channel = randomAccessFile.getChannel();
            fileLock = channel.lock();
        }
        catch (IOException e)
        {
            throw new FileConnectionIOException(file, e);
        }
        finally
        {
            if (fileLock == null)
                close();
        }
    }

    public void write(long number, T obj)
    {
        checkOpened();
        MappedByteBuffer byteBuffer = getMappedByteBuffer(number / batchSize);
        byteBuffer.position((int) (number % batchSize) * objectLength);
        serializer.serialize(obj, byteBuffer);
    }

    public T read(long number)
    {
        checkOpened();
        MappedByteBuffer byteBuffer = getMappedByteBuffer(number / batchSize);
        byteBuffer.position((int) (number % batchSize) * objectLength);
        return serializer.deserialize(byteBuffer);
    }

    protected void finalize() throws Throwable
    {
        close();
        super.finalize();
    }

    private void checkOpened()
    {
        if (channel == null)
            throw new ConnectionNotOpenedYetException();
    }

    private MappedByteBuffer getMappedByteBuffer(long number)
    {
        while (mappedByteBuffers.size() <= number)
            mappedByteBuffers.add(createMappedBuffer(mappedByteBuffers.size()));
        return mappedByteBuffers.get((int) number);
    }

    private MappedByteBuffer createMappedBuffer(long number)
    {
        try
        {
            return channel.map(FileChannel.MapMode.READ_WRITE, number * batchSize * objectLength, batchSize * objectLength);
        }
        catch (IOException e)
        {
            throw new FileConnectionIOException(file, e);
        }
    }
}
