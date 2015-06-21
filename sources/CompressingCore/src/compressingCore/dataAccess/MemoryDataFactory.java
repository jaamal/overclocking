package compressingCore.dataAccess;

import org.apache.commons.io.IOUtils;

import data.charArray.CharArray;
import data.charArray.IReadableCharArray;
import data.enumerableData.InMemoryEnumerableData;
import data.longArray.ILongArray;
import data.longArray.LongArray;
import dataContracts.DataFactoryType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class MemoryDataFactory implements ITypedDataFactory
{
    @Override
    public IReadableCharArray getCharArray(Path filePath)
    {
        try
        {
            try(BufferedReader reader = Files.newBufferedReader(filePath, Charset.defaultCharset())) {
                char[] chars = IOUtils.toCharArray(reader);
                return createCharArray(chars);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IReadableCharArray createCharArray(char[] chars) {
        InMemoryEnumerableData<Character> charsData = new InMemoryEnumerableData<Character>(Character.class, chars.length);
        for (int i = 0; i < chars.length; i++) {
            charsData.save(i, chars[i]);
        }
        return new CharArray(charsData, chars.length);
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
