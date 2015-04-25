package compressionservice.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;

import dataContracts.DataFactoryType;

public interface IFactorIteratorFactory
{
    IFactorIterator createWindowIterator(IReadableCharArray charArray, int windowSize);
    IFactorIterator createInfiniteIterator(IReadableCharArray charArray, DataFactoryType dataFactoryType);
}
