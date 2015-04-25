package compressionservice.runner;

import compressionservice.runner.parameters.IRunParams;

public interface IWorker
{
    void process(IRunParams runParams);
}
