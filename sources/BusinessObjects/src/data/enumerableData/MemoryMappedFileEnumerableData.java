package data.enumerableData;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;

import commons.files.IFile;
import commons.files.IFileManager;
import commons.settings.ISettings;
import commons.settings.KnownKeys;

public class MemoryMappedFileEnumerableData<T> implements IEnumerableData<T>
{
    private final boolean deleteOnClose;
    private final int batchSize;
    private final int objectLength;
    private final int bufferLength;
    
    private IFile file;
    private ArrayList<MappedByteBuffer> buffersCache;
    private IItemSerializer<T> serializer;

    public MemoryMappedFileEnumerableData(
            IItemSerializer<T> serializer,
            IFileManager fileManager,
            ISettings settings)
    {
        this(serializer, fileManager.createTempFile(), settings, true);
    }

    public MemoryMappedFileEnumerableData(
            IItemSerializer<T> serializer,
            IFile file,
            ISettings settings)
    {
        this(serializer, file, settings, false);
    }

    private MemoryMappedFileEnumerableData(
            IItemSerializer<T> serializer,
            IFile file,
            ISettings settings,
            boolean deleteOnClose)
    {
        this.batchSize = settings.getInt(KnownKeys.MemoryMappedFileBatchSize);
        
        this.file = file;
        this.serializer = serializer;
        this.deleteOnClose = deleteOnClose;
        this.buffersCache = new ArrayList<MappedByteBuffer>();
        
        objectLength = serializer.itemSizeInBytes();
        bufferLength = objectLength * batchSize;
    }

    @Override
    public T load(long index)
    {
        if (index == -1)
            return null;
        checkIndex(index);
        
        MappedByteBuffer byteBuffer = getOrCreateBuffer(index / batchSize);
        byteBuffer.position((int) (index % batchSize) * objectLength);
        return serializer.deserialize(byteBuffer);
    }

    @Override
    public void save(long index, T obj)
    {
        checkIndex(index);
        
        MappedByteBuffer buffer = getOrCreateBuffer(index / batchSize);
        buffer.position((int) (index % batchSize) * objectLength);
        serializer.serialize(obj, buffer);
    }

    @Override
    public void close()
    {
        for (MappedByteBuffer buffer : buffersCache)
            buffer.force();
        if (deleteOnClose)
            file.delete();
        else
            file.close();
    }

    @Override
    protected void finalize() throws Throwable
    {
        close();
        super.finalize();
    }
    
    private MappedByteBuffer getOrCreateBuffer(long index)
    {
        while (buffersCache.size() <= index) {
            try {
                buffersCache.add(file.mapBuffer(index * batchSize * objectLength, bufferLength));
            }
            catch (IOException e) {
                throw new RuntimeException(String.format("Fail to create map for file %s.", file.getPath()), e);
            }
        }
        return buffersCache.get((int) index);
    }

    private static void checkIndex(long number)
    {
        if (number < 0)
            throw new IndexOutOfBoundsException();
    }
}
