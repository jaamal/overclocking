package compressingCore.dataAccess;

import commons.files.IFile;

import java.io.IOException;

public class FileReadableCharArray implements IReadableCharArray
{
    private IFile file;
    private byte[] buffer;
    private final int bufferLength = 16 * 1024;
    private long currentOffset;
    private long offset;
    private long length;

    public FileReadableCharArray(IFile file)
    {
        this(file, 0, file.size());
    }

    private FileReadableCharArray(IFile file, long offset, long length)
    {
        this.file = file;
        buffer = new byte[bufferLength];
        currentOffset = Long.MAX_VALUE;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public char get(long idx)
    {
        if (!(idx >= currentOffset && idx < currentOffset + bufferLength))
        {
            currentOffset = idx;
            try
            {
                file.read(idx + offset, buffer);
            }
            catch (IOException e)
            {
                file.close();
                throw new RuntimeException("Error with file in method get(long)", e);
            }
        }
        return (char) buffer[(int) (idx - currentOffset)];
    }

    @Override
    public long length()
    {
        return length;
    }

    @Override
    public IReadableCharArray subArray(long inclusiveStartIndex, long exclusiveEndIndex)
    {
        return new FileReadableCharArray(file, inclusiveStartIndex, exclusiveEndIndex - inclusiveStartIndex);
    }

    @Override
    public void close()
    {
        file.close();
    }
}
