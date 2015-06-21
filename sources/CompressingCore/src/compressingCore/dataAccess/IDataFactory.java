package compressingCore.dataAccess;

import java.nio.file.Path;

import data.charArray.IReadableCharArray;
import data.longArray.ILongArray;
import dataContracts.DataFactoryType;

public interface IDataFactory
{
    public IReadableCharArray getCharArray(DataFactoryType dataType, Path filePath);
    public IReadableCharArray createCharArray(char[] chars);
    public ILongArray createLongArray(DataFactoryType dataType, long size);
}
