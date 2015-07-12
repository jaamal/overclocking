package data.enumerableData;

import java.io.File;

import commons.files.IFileManager;
import commons.settings.ISettings;

public class MemoryMappedFileEnumerableData<T> implements IEnumerableData<T>
{
    private MemoryMappedFileConnection<T> connection;

    public MemoryMappedFileEnumerableData(
            IItemSerializer<T> serializer,
            IFileManager fileManager,
            ISettings settings)
    {
        this(serializer, fileManager.createTempFile(), settings, true);
    }

    public MemoryMappedFileEnumerableData(
            IItemSerializer<T> serializer,
            File file,
            ISettings settings)
    {
        this(serializer, file, settings, false);
    }

    public MemoryMappedFileEnumerableData(
            IItemSerializer<T> serializer,
            File file,
            ISettings settings,
            boolean deleteOnClose)
    {
        this.connection = new MemoryMappedFileConnection<T>(file, settings, serializer, deleteOnClose);
        connection.open();
    }

    @Override
    public T load(long number)
    {
        if (number == -1)
            return null;
        checkIndex(number);
        return connection.read(number);
    }

    @Override
    public void save(long number, T obj)
    {
        checkIndex(number);
        connection.write(number, obj);
    }

    @Override
    public void close()
    {
        connection.close();
    }

    @Override
    protected void finalize() throws Throwable
    {
        close();
        super.finalize();
    }

    private static void checkIndex(long number)
    {
        if (number < 0)
            throw new IndexOutOfBoundsException();
    }
}
