package compressionservice.compression.algorithms;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.parameters.ICompressionRunParams;

import dataContracts.FactorDef;

public interface IResourceProvider {
    FactorDef[] getFactorization(ICompressionRunParams runParams);
    IReadableCharArray getText(ICompressionRunParams runParams);
}
