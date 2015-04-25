package compressionservice.compression.running;

import compressionservice.compression.parameters.IRunParams;

public interface IWorker
{
    void process(IRunParams runParams);
}
