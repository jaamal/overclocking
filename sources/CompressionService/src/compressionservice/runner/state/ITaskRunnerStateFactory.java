package compressionservice.runner.state;

import java.util.UUID;

public interface ITaskRunnerStateFactory {
    TaskStateModel inProgress();
    TaskStateModel failed(UUID requestId, String message);
    TaskStateModel complete(UUID requestId);
}
