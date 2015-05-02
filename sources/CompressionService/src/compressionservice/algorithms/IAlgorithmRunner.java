package compressionservice.algorithms;

import compressionservice.runner.parameters.IRunParams;
import dataContracts.statistics.StatisticsObject;

public interface IAlgorithmRunner {
    
    StatisticsObject run(IRunParams runParams);
}
