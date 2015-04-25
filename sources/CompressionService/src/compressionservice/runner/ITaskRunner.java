package compressionservice.runner;

import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.state.TaskRunnerState;

public interface ITaskRunner {
    TaskRunnerState run(IRunParams runParams);
    boolean isAvailable();
}
