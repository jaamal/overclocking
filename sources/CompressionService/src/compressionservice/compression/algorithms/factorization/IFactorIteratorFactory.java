package compressionservice.compression.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.parameters.IRunParams;

public interface IFactorIteratorFactory
{
    IFactorIterator create(IRunParams runParams, IReadableCharArray charArray);
}
