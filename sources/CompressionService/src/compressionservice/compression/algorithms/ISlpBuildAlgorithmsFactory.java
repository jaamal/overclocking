package compressionservice.compression.algorithms;

import compressionservice.compression.parameters.ICompressionRunParams;


public interface ISlpBuildAlgorithmsFactory {
    ISlpBuildAlgorithm create(ICompressionRunParams runParams);
}
