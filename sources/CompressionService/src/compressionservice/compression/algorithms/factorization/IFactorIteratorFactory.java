package compressionservice.compression.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;

public interface IFactorIteratorFactory
{
    IFactorIterator create(AlgorithmType algorithmType, DataFactoryType dataFactoryType, IReadableCharArray charArray);
}
