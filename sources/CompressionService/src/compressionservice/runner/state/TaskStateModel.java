package compressionservice.runner.state;

import java.util.UUID;

public class TaskStateModel {
    public final UUID requestId;
    public final TaskState state;
    public final String message;
    
    public TaskStateModel(UUID requestId, TaskState state, String message) {
        this.requestId = requestId;
        this.state = state;
        this.message = message;
    }
}
