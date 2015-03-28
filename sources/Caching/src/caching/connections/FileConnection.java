package caching.connections;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class FileConnection implements IConnection
{
    private static byte[] zeroBatch = new byte[1 << 16];

	private final File file;
	private RandomAccessFile raFile;
	private FileLock fileLock;

	public FileConnection(File file) {
		this.file = file;
		this.raFile = null;
		this.fileLock = null;
	}

	@Override
	public void close()
	{
		if (raFile == null)
			return;

		try {
			if (fileLock != null)
				fileLock.release();
			raFile.close();
            file.delete();
		}
		catch (IOException e) {
			throw new FileConnectionIOException(file, e);
		}
		finally {
			raFile = null;
			fileLock = null;
		}
	}

	@Override
	public void open()
	{
		if (raFile != null)
			return;
		try {
			raFile = new RandomAccessFile(file, "rw");
			fileLock = raFile.getChannel().lock();
		}
		catch (IOException e) {
			throw new FileConnectionIOException(file, e);
		}
		finally {
			if (fileLock == null)
				close();
		}
	}

	@Override
    public void write(long offset, byte[] array)
    {
		checkOpened();
        increaseLength(offset);
		try {
            raFile.seek(offset);
			raFile.write(array);
		}
		catch (IOException e) {
			throw new FileConnectionIOException(file, e);
		}
	}

	@Override
	public byte[] read(long offset, int length)
	{
		checkOpened();
        increaseLength(offset + length);
		try {
            raFile.seek(offset);
			byte[] array = new byte[length];
			raFile.read(array);
			return array;
		}
		catch (IOException e) {
			throw new FileConnectionIOException(file, e);
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		close();
		super.finalize();
	}

    private void checkOpened()
	{
		if (raFile == null)
			throw new ConnectionNotOpenedYetException();
	}

    private void increaseLength(long length)
    {
        try {
            if (raFile.length() >= length)
                return ;
            long lost = length - raFile.length();
            raFile.seek(raFile.length());
            while (lost > 0)
            {
                int batch = (int)Math.min(zeroBatch.length, lost);
                raFile.write(zeroBatch, 0, batch);
                lost -= batch;
            }
        } catch(IOException e)
        {
             throw new FileConnectionIOException(file, e);
        }
    }
}
