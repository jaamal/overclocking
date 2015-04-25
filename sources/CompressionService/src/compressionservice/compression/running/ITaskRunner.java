package compressionservice.compression.running;

import compressionservice.businessObjects.TaskRunnerState;
import compressionservice.compression.parameters.IRunParams;

public interface ITaskRunner {
    TaskRunnerState run(IRunParams runParams);
    boolean isAvailable();
}
