package compressionservice.algorithms.lzInf.suffixArray;

import java.io.IOException;

import commons.files.IFile;
import commons.files.IFileManager;
import commons.settings.ISettings;
import commons.settings.KnownKeys;
import commons.utils.NumericUtils;

import data.IDataFactory;
import data.charArray.IReadableCharArray;
import data.longArray.ILongArray;
import dataContracts.DataFactoryType;

public class SuffixArrayBuilder implements ISuffixArrayBuilder
{
    private IFileManager fileManager;
    private IExternalProcessExecutor externalProcessExecutor;
    private ISettings settings;
    private IDataFactory dataFactory;

    public SuffixArrayBuilder(
            IDataFactory dataFactory,
            IFileManager fileManager,
            IExternalProcessExecutor externalProcessExecutor,
            ISettings settings)
    {
        this.dataFactory = dataFactory;
        this.fileManager = fileManager;
        this.externalProcessExecutor = externalProcessExecutor;
        this.settings = settings;
    }
    
    @Override
    public ISuffixArray build(DataFactoryType dataFactoryType, IReadableCharArray source) {
        try (IFile textFile = fileManager.createTempFile();
             IFile suffixArrayFile = fileManager.createTempFile())
        {
            final long textSize = source.length();
            source.saveToFile(textFile);
            String processPath = settings.getPath(KnownKeys.ServerSuffixArrayBuilderPath).toString();
            externalProcessExecutor.execute(processPath, new String[] { textFile.getPathStr(), String.valueOf(textSize), suffixArrayFile.getPathStr() });
            
            ILongArray longArray = dataFactory.createLongArray(dataFactoryType, textSize);
            byte[] buffer = new byte[128 * 1024];
            long position = 0;
            long index = 0;
            while (position < 4 * textSize)
            {
                int actual = suffixArrayFile.read(position, buffer);
                final int right = actual / 4;
                for (int i = 0; i < right; i++)
                    longArray.set(index++, NumericUtils.bytesToInt(buffer, i * 4));
                position += actual;
            }

            SuffixArray result = new SuffixArray(longArray, source);
            suffixArrayFile.delete();
            textFile.delete();
            return result;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
