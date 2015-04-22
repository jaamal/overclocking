package compressionservice.compression.algorithms;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.parameters.IRunParams;

import dataContracts.FactorDef;

public interface IResourceProvider {
    FactorDef[] getFactorization(IRunParams runParams);
    IReadableCharArray getText(IRunParams runParams);
}
