package compressionservice.runner;

import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.state.TaskStateModel;

public interface ITaskRunner {
    TaskStateModel run(IRunParams runParams);
    boolean isAvailable();
}
