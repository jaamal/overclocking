package compressionservice.algorithms;

import dataContracts.statistics.IStatistics;

public interface IAlgorithmRunner {
    
    IStatistics run(String resultId);
}
