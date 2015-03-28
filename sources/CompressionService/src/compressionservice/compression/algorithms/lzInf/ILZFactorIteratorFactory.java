package compressionservice.compression.algorithms.lzInf;

import compressingCore.dataAccess.IReadableCharArray;
import dataContracts.DataFactoryType;

public interface ILZFactorIteratorFactory
{
    ILZFactorIterator create(DataFactoryType dataFactoryType, IReadableCharArray charArray);
}
