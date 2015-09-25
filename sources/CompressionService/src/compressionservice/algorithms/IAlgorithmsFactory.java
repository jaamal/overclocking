package compressionservice.algorithms;

import compressionservice.runner.parameters.IRunParams;

public interface IAlgorithmsFactory {
    IAlgorithm create(IRunParams runParams);
    
    //TODO: ugly method
    Iterable<String> getAllSourceIds(IRunParams runParams);
}
