package compressionservice.compression;

import java.util.UUID;

import compressionservice.businessObjects.TaskRunnerState;

public interface ITasksRunnerStatesStorage {
    TaskRunnerState registerNew();
    TaskRunnerState setComplete(UUID requestId);
    TaskRunnerState setFailed(UUID requestId, String message);
    TaskRunnerState getState(UUID requestId);
    TaskRunnerState[] getAll();
}
