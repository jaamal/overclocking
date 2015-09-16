package compressionservice.algorithms;

import dataContracts.statistics.IStatistics;

public interface IAlgorithmRunner {
    
    void run();
    IStatistics getStats();
}
