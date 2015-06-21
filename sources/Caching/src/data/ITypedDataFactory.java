package data;

import java.nio.file.Path;

import data.charArray.IReadableCharArray;
import data.longArray.ILongArray;
import dataContracts.DataFactoryType;

public interface ITypedDataFactory {

    DataFactoryType getDataType();
    IReadableCharArray getCharArray(Path filePath);
    IReadableCharArray createCharArray(char[] chars);
    ILongArray createLongArray(long size);
}
