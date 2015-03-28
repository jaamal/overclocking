package caching;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;

import caching.connections.ConnectionNotOpenedYetException;
import caching.connections.FileConnectionIOException;
import caching.connections.ITemporaryFileFactory;
import caching.serializers.ISerializer;

import commons.settings.ISettings;
import commons.settings.KnownKeys;

public class MemoryMappedFileStorage<T> implements IStorage<T>
{
	private MemoryMappedFileConnection connection;
	private ISerializer<T> serializer;

	public MemoryMappedFileStorage(
			ISerializer<T> serializer,
			ITemporaryFileFactory temporaryFileFactory,
			ISettings settings)
	{
		this.serializer = serializer;
		this.connection = new MemoryMappedFileConnection(temporaryFileFactory.getTemporaryFile(), settings, serializer.size(), true);
		connection.open();
	}

	public MemoryMappedFileStorage(
	        ISerializer<T> serializer,
			File file,
			ISettings settings)
	{
		this.serializer = serializer;
		this.connection = new MemoryMappedFileConnection(file, settings, serializer.size(), false);
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

	private class MemoryMappedFileConnection
	{
		private final File file;
		private FileLock fileLock;
		private ArrayList<MappedByteBuffer> mappedByteBuffers;
		private FileChannel channel;
		private int batchSize;
		private int objectLength;
		private boolean deleteOnClose;
		private RandomAccessFile randomAccessFile;

		public MemoryMappedFileConnection(
				File file,
				ISettings settings,
				int objectLength,
				boolean deleteOnClose)
		{
			this.objectLength = objectLength;
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

		public void write(long number, byte[] array)
		{
			if (array.length != objectLength)
				throw new RuntimeException(String.format("Invalid length of array %d. It must be equals %d", array.length, objectLength));
			checkOpened();
			MappedByteBuffer mappedByteBuffer = getMappedByteBuffer(number / batchSize);
			mappedByteBuffer.position((int) (number % batchSize) * objectLength);
			mappedByteBuffer.put(array);
		}

		public byte[] read(long number)
		{
			checkOpened();
			MappedByteBuffer mappedByteBuffer = getMappedByteBuffer(number / batchSize);
			mappedByteBuffer.position((int) (number % batchSize) * objectLength);
			byte[] array = new byte[objectLength];
			mappedByteBuffer.get(array);
			return array;
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
}
