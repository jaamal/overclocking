package compressionservice.algorithms;

import dataContracts.AlgorithmType;
import dataContracts.statistics.IStatistics;

public interface IAlgorithm {
    
    void run();
    AlgorithmType getType();
    IStatistics getStats();
}
