package compressingCore.dataAccess;

import org.apache.commons.io.IOUtils;

import data.enumerableData.InMemoryEnumerableData;
import dataContracts.DataFactoryType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class MemoryDataFactory implements ITypedDataFactory
{
    @Override
    public IReadableCharArray readFile(Path filePath)
    {
        try
        {
            try(BufferedReader reader = Files.newBufferedReader(filePath, Charset.defaultCharset())) {
                char[] chars = IOUtils.toCharArray(reader);
                return new MemoryReadableCharArray(chars, 0, chars.length);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ILongArray createLongArray(long size)
    {
        return new LongArray(new InMemoryEnumerableData<Long>(Long.class), size);
    }

    @Override
    public DataFactoryType getDataType() {
        return DataFactoryType.memory;
    }
}
