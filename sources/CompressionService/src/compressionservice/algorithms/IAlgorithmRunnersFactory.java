package compressionservice.algorithms;

import compressionservice.runner.parameters.IRunParams;

public interface IAlgorithmRunnersFactory {
    IAlgorithm create(IRunParams runParams);
    
    //TODO: ugly method
    Iterable<String> getAllSourceIds(IRunParams runParams);
}
