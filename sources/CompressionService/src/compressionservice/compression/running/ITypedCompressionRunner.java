package compressionservice.compression.running;

import compressionservice.compression.parameters.ICompressionRunParams;

import dataContracts.AlgorithmType;

public interface ITypedCompressionRunner {
    AlgorithmType getAlgorithmType();

    CheckParamsResult checkAndRefillParams(ICompressionRunParams runParams);

    void run(ICompressionRunParams runParams);
}

