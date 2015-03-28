package compressionservice.compression.algorithms;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.parameters.ICompressionRunParams;
import dataContracts.LZFactorDef;

public interface IResourceProvider {
    LZFactorDef[] getFactorization(ICompressionRunParams runParams);
    IReadableCharArray getText(ICompressionRunParams runParams);
}
