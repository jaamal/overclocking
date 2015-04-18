package compressionservice.compression.algorithms;

import compressionservice.compression.parameters.ICompressionRunParams;
import dataContracts.statistics.StatisticsObject;

public interface IAlgorithmRunner {
    
    StatisticsObject run(ICompressionRunParams runParams);
    Iterable<String> getAllSourceIds();
}
