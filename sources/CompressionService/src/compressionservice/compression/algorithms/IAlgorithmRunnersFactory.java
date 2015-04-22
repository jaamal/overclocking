package compressionservice.compression.algorithms;

import compressionservice.compression.parameters.IRunParams;

public interface IAlgorithmRunnersFactory {
    IAlgorithmRunner create(IRunParams runParams);
}
