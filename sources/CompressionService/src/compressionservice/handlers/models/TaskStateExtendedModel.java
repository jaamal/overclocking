package compressionservice.handlers.models;

import java.util.UUID;

import compressionservice.runner.state.TaskState;
import compressionservice.runner.state.TaskStateModel;

public class TaskStateExtendedModel
{
    public final UUID requestId;
    public final TaskState state;
    public final String message;
    public final String opLog;
    
    public TaskStateExtendedModel(TaskStateModel taskStateModel, String opLog) {
        this.requestId = taskStateModel.requestId;
        this.state = taskStateModel.state;
        this.message = taskStateModel.message;
        this.opLog = opLog;
    }
}
