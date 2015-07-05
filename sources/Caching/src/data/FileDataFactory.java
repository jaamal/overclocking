package data;

import java.io.File;
import java.nio.file.Path;

import caching.connections.ITemporaryFileFactory;
import commons.settings.ISettings;
import data.charArray.CharArray;
import data.charArray.CharSerializer;
import data.charArray.IReadableCharArray;
import data.enumerableData.IItemSerializer;
import data.enumerableData.MemoryMappedFileEnumerableData;
import data.longArray.ILongArray;
import data.longArray.LongArray;
import data.longArray.LongSerializer;
import dataContracts.DataFactoryType;

public class FileDataFactory implements ITypedDataFactory
{
    private static IItemSerializer<Long> longSerializer = new LongSerializer();
    private static IItemSerializer<Character> charSerializer = new CharSerializer();
    private ITemporaryFileFactory temporaryFileFactory;
    private ISettings settings;

    public FileDataFactory(
            ITemporaryFileFactory temporaryFileFactory,
            ISettings settings)
    {
        this.temporaryFileFactory = temporaryFileFactory;
        this.settings = settings;
    }

    @Override
    public IReadableCharArray getCharArray(Path filePath)
    {
        File file = filePath.toFile();
        MemoryMappedFileEnumerableData<Character> memoryMappedFileStorage = new MemoryMappedFileEnumerableData<>(charSerializer, file, settings);
        return new CharArray(memoryMappedFileStorage, file.length());
    }

    @Override
    public ILongArray createLongArray(long size)
    {
        MemoryMappedFileEnumerableData<Long> memoryMappedFileStorage = new MemoryMappedFileEnumerableData<>(longSerializer, temporaryFileFactory, settings);
        return new LongArray(memoryMappedFileStorage, size);
    }

    @Override
    public DataFactoryType getDataType() {
        return DataFactoryType.file;
    }

    @Override
    public IReadableCharArray createCharArray(char[] chars) {
        throw new RuntimeException("Unable to create char array implicitly at file data factory.");
    }
}