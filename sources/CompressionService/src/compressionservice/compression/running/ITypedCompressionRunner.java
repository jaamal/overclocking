package compressionservice.compression.running;

import compressionservice.compression.parameters.IRunParams;

import dataContracts.AlgorithmType;

public interface ITypedCompressionRunner {
    AlgorithmType getAlgorithmType();

    void run(IRunParams runParams);
}

