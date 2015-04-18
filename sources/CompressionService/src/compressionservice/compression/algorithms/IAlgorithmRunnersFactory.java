package compressionservice.compression.algorithms;

import compressionservice.compression.parameters.ICompressionRunParams;

public interface IAlgorithmRunnersFactory {
    IAlgorithmRunner create(ICompressionRunParams runParams);
}
