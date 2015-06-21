package compressingCore.dataAccess;

import java.io.File;
import java.nio.file.Path;

import caching.MemoryMappedFileEnumerableData;
import caching.connections.ITemporaryFileFactory;
import caching.serializers.ISerializer;
import commons.settings.ISettings;
import dataContracts.DataFactoryType;

public class FileDataFactory implements ITypedDataFactory
{
    private static ISerializer<Long> longSerializer = new LongSerializer();
    private static ISerializer<Character> charSerializer = new CharSerializer();
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
    public IReadableCharArray readFile(Path filePath)
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
}
