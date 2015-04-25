package compressionservice.runner.state;

import java.util.UUID;

public interface ITasksRunnerStatesStorage {
    TaskRunnerState registerNew();
    TaskRunnerState setComplete(UUID requestId);
    TaskRunnerState setFailed(UUID requestId, String message);
    TaskRunnerState getState(UUID requestId);
    TaskRunnerState[] getAll();
}
