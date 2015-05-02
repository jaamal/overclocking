package compressionservice.runner.parameters;

import java.util.Map;

import dataContracts.AlgorithmType;

public interface IRunParamsFactory {
    IRunParams create(Map<String, String> parameters);
    IRunParams create(String sourceId, AlgorithmType algorithmType);
    IRunParams create(AlgorithmType algorithmType);
}
