package compressingCore.dataAccess;

import java.nio.file.Path;

import dataContracts.DataFactoryType;

public interface ITypedDataFactory {

    DataFactoryType getDataType();
    IReadableCharArray readFile(Path filePath);
    ILongArray createLongArray(long size);
}
