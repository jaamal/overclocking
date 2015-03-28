package compressionservice.compression.algorithms;

import compressionservice.compression.parameters.ICompressionRunParams;
import dataContracts.statistics.StatisticsObject;

public interface ISlpBuildAlgorithm {
    StatisticsObject build(ICompressionRunParams runParams);

    Iterable<String> getAllSourceIds();
}
