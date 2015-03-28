package compressingCore.dataAccess;

import java.nio.file.Path;

import dataContracts.DataFactoryType;

public interface IDataFactory
{
    public IReadableCharArray readFile(DataFactoryType dataType, Path filePath);
    public ILongArray createLongArray(DataFactoryType dataType, long size);
}
