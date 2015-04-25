package compressionservice.algorithms;

import compressionservice.runner.parameters.IRunParams;

public interface IAlgorithmRunnersFactory {
    IAlgorithmRunner create(IRunParams runParams);
}
