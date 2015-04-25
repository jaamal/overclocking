package compressionservice.runner.state;

import java.util.UUID;

import dataContracts.IIDFactory;

public class TaskRunnerStateFactory implements ITaskRunnerStateFactory {

    private IIDFactory idFactory;

    public TaskRunnerStateFactory(IIDFactory idFactory) {
        this.idFactory = idFactory;
    }
    
    @Override
    public TaskStateModel inProgress() {
        return new TaskStateModel(idFactory.create(), TaskState.InProgress, "");
    }

    @Override
    public TaskStateModel failed(String message) {
        return failed(idFactory.getEmpty(), message);
    }

    @Override
    public TaskStateModel failed(UUID requestId, String message) {
        return new TaskStateModel(requestId, TaskState.Failed, message);
    }
    
    @Override
    public TaskStateModel complete(UUID requestId) {
        return new TaskStateModel(requestId, TaskState.Complete, "");
    }

}
