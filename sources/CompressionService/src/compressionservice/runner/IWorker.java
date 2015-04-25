package compressionservice.runner;

import java.util.UUID;

import compressionservice.runner.parameters.IRunParams;

public interface IWorker
{
    void process(UUID requestId, IRunParams runParams);
}
