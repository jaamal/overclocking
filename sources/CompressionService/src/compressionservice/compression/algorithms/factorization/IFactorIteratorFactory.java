package compressionservice.compression.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.parameters.ICompressionRunParams;

public interface IFactorIteratorFactory
{
    IFactorIterator create(ICompressionRunParams runParams, IReadableCharArray charArray);
}
