package compressionservice.compression.algorithms;

import compressionservice.compression.parameters.IRunParams;
import dataContracts.statistics.StatisticsObject;

public interface IAlgorithmRunner {
    
    StatisticsObject run(IRunParams runParams);
    Iterable<String> getAllSourceIds();
}
