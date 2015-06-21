package data.enumerableData;

import java.io.File;

import caching.connections.ITemporaryFileFactory;
import commons.settings.ISettings;

public class MemoryMappedFileEnumerableData<T> implements IEnumerableData<T>
{
	private MemoryMappedFileConnection connection;
	private IItemSerializer<T> serializer;

	public MemoryMappedFileEnumerableData(
            IItemSerializer<T> serializer,
            ITemporaryFileFactory temporaryFileFactory,
            ISettings settings)
    {
        this(serializer, temporaryFileFactory.getTemporaryFile(), settings, true);
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
        this.serializer = serializer;
        this.connection = new MemoryMappedFileConnection(file, settings, serializer.itemSizeInBytes(), deleteOnClose);
        connection.open();
    }

	@Override
	public T load(long number)
	{
		if (number == -1)
			return null;
		checkIndex(number);
		return serializer.deserialize(connection.read(number));
	}

	@Override
	public void save(long number, T obj)
	{
		checkIndex(number);
		connection.write(number, serializer.serialize(obj));
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
