package compressionservice.businessObjects;

import java.util.UUID;

public class TaskRunnerState {
    public final UUID requestId;
    public final TaskState state;
    public final String message;
    
    public TaskRunnerState(UUID requestId, TaskState state, String message) {
        this.requestId = requestId;
        this.state = state;
        this.message = message;
    }
}
