package compressionservice.algorithms.lzInf.suffixArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import commons.files.IFile;
import commons.files.IFileManager;
import commons.settings.ISettings;
import commons.settings.KnownKeys;
import compressingCore.dataAccess.IDataFactory;
import compressingCore.dataAccess.ILongArray;
import compressingCore.dataAccess.IReadableCharArray;
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
    public ISuffixArray build(DataFactoryType dataFactoryType, String pathToFile) {
        try (IFile textFile = fileManager.getFile(pathToFile); ) {
            IReadableCharArray source = dataFactory.readFile(dataFactoryType, new File(pathToFile).toPath());
            return build(dataFactoryType, source);
        }
    }
    
    @Override
    public ISuffixArray build(DataFactoryType dataFactoryType, IReadableCharArray source) {
      //TODO cheat, fix it
        try (IFile textFile = fileManager.createTempFile(settings.getPath(KnownKeys.ServerWorkingDir).toString());)
        {
            saveToFile(textFile, source);
            final long textSize = source.length();
            String processPath = settings.getPath(KnownKeys.ServerSuffixArrayBuilderPath).toString();
            InputStream suffixArrayStream = externalProcessExecutor.execute(processPath, new String[] { textFile.getFileName(), String.valueOf(textSize) });
            
            ILongArray suffixArray = dataFactory.createLongArray(dataFactoryType, textSize);
            long index = 0;
            if (suffixArrayStream.available() > 0){
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(suffixArrayStream));) {
                    String line;
                    do {
                        line = reader.readLine();
                        if (line != null) {
                            String[] splits = line.split(",");
                            for (int i = 0; i < splits.length; i++) {
                                suffixArray.set(index, Long.parseLong(splits[i]));
                                index++;
                            }
                        }
                    }
                    while (line != null);
                }
            }
            suffixArrayStream.close();
            return new SuffixArray(suffixArray, source);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
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
}
