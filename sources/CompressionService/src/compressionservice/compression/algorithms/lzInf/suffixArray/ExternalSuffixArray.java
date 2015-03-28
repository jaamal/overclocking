package compressionservice.compression.algorithms.lzInf.suffixArray;

import commons.files.IFile;
import commons.files.IFileManager;
import commons.settings.ISettings;
import commons.settings.KnownKeys;
import commons.utils.NumericUtils;
import compressingCore.dataAccess.IDataFactory;
import compressingCore.dataAccess.ILongArray;
import compressingCore.dataAccess.IReadableCharArray;

import java.io.IOException;

import dataContracts.DataFactoryType;

public class ExternalSuffixArray implements ISuffixArray
{
    private ILongArray suffixArray;
    private IExternalProcessExecutor externalProcessExecutor;
    private IReadableCharArray charArray;
    private IDataFactory dataFactory;
    private IFileManager fileManager;
    private long length;
    private ISettings settings;
    private DataFactoryType dataFactoryType;

    public ExternalSuffixArray(
            IReadableCharArray charArray,
            IDataFactory dataFactory,
            IFileManager fileManager,
            IExternalProcessExecutor externalProcessExecutor,
            ISettings settings, 
            DataFactoryType dataFactoryType)
    {
        this.charArray = charArray;
        this.dataFactory = dataFactory;
        this.fileManager = fileManager;
        this.externalProcessExecutor = externalProcessExecutor;
        this.settings = settings;
        this.dataFactoryType = dataFactoryType;

        build();

        length = charArray.length();
    }

    private void build()
    {
        //TODO cheat, fix it
        IFile stringFile = fileManager.createTempFile(settings.getPath(KnownKeys.ServerWorkingDir).toString());
        IFile arrayFile = fileManager.createTempFile(settings.getPath(KnownKeys.ServerWorkingDir).toString());

        try
        {
            saveToFile(stringFile, charArray);
            final long size = charArray.length();
            String commandLine = settings.getPath(KnownKeys.ServerSuffixArrayBuilderPath)
                    + " \"" + stringFile.getFileName() + "\" " + size + " \"" + arrayFile.getFileName() + "\"";
            final int exitCode = externalProcessExecutor.execute(commandLine);
            if (exitCode != 0)
                throw new ExternalProcessCrashesException(exitCode);
            suffixArray = dataFactory.createLongArray(dataFactoryType, size);
            byte[] buffer = new byte[128 * 1024];
            long position = 0;
            long index = 0;
            while (position < 4 * size)
            {
                int actual = arrayFile.read(position, buffer);
                final int right = actual / 4;
                for (int i = 0; i < right; i++)
                    suffixArray.set(index++, NumericUtils.bytesToInt(buffer, i * 4));
                position += actual;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            stringFile.remove();
            arrayFile.remove();
        }
    }

    private static void saveToFile(IFile file, IReadableCharArray readableCharArray) throws IOException
    {
        char[] buffer = new char[16 * 1024];
        int currentBufferSize = 0;
        long charArraySize = readableCharArray.length();
        for (long i = 0; i < charArraySize; i++)
        {
            if (currentBufferSize == buffer.length)
            {
                file.appendBatch(buffer, 0, currentBufferSize);
                currentBufferSize = 0;
            }
            buffer[currentBufferSize++] = readableCharArray.get(i);
        }
        if (currentBufferSize > 0)
        {
            file.appendBatch(buffer, 0, currentBufferSize);
        }
    }

    @Override
    public IReadableCharArray getSource()
    {
        return charArray;
    }

    @Override
    public long get(long pos)
    {
        if (pos < 0 || pos >= length)
            throw new IndexOutOfBoundsException(Long.toString(pos));
        return suffixArray.get(pos);
    }

    @Override
    public long length()
    {
        return length;
    }

    @Override
    public void dispose()
    {
        suffixArray.close();
    }

    @Override
    public ILongArray getInnerArray()
    {
        return suffixArray;
    }
}
