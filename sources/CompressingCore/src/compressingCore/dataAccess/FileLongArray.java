package compressingCore.dataAccess;

import commons.files.IFile;
import commons.files.IFileManager;
import commons.utils.NumericUtils;

import java.io.IOException;

public class FileLongArray implements ILongArray
{
    private IFile file;
    private byte[] buffer;
    private final int bufferLength = 2 * 1024;
    private long currentOffset;
    private long length;

    public FileLongArray(IFileManager fileManager, String workingDir, long length)
    {
        this.file = fileManager.createTempFile(workingDir);
        buffer = new byte[bufferLength << 3];
        this.length = length;
        currentOffset = 0;
        try
        {
            file.read(0, buffer);
        }
        catch (IOException e)
        {
            file.remove();
            throw new RuntimeException("Error while work with file " + file.getPath(), e);
        }
    }

    @Override
    public long get(long index)
    {
        actualizeCache(index);
        return NumericUtils.bytesToLong(buffer, (int) (index - currentOffset) << 3);
    }

    private void actualizeCache(long index)
    {
        if (!(index >= currentOffset && index < currentOffset + bufferLength))
        {
            try
            {
                file.write(currentOffset << 3, buffer, 0, (int) Math.min((long) buffer.length, (length - currentOffset) << 3));
                currentOffset = index;
                file.read(index << 3, buffer);
            }
            catch (IOException e)
            {
                file.remove();
                throw new RuntimeException("Error while work with file " + file.getPath(), e);
            }
        }
    }

    @Override
    public void set(long index, long value)
    {
        actualizeCache(index);
        NumericUtils.longToBytes(value, buffer, (int) (index - currentOffset) << 3);
    }

    @Override
    public long size()
    {
        return length;
    }

    @Override
    public void close()
    {
        file.remove();
    }
}
