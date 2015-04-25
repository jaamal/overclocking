package compressionservice.runner.state;

import java.util.UUID;

import dataContracts.IIDFactory;

public class CompressionRunnerStateFactory implements ICompressionRunnerStateFactory {

    private IIDFactory idFactory;

    public CompressionRunnerStateFactory(IIDFactory idFactory) {
        this.idFactory = idFactory;
    }
    
    @Override
    public TaskRunnerState createNew() {
        return new TaskRunnerState(idFactory.create(), TaskState.InProgress, "");
    }

    @Override
    public TaskRunnerState createFailed(String message) {
        return createFailed(idFactory.getEmpty(), message);
    }

    @Override
    public TaskRunnerState createFailed(UUID requestId, String message) {
        return new TaskRunnerState(requestId, TaskState.Failed, message);
    }
    
    @Override
    public TaskRunnerState createComplete(UUID requestId) {
        return new TaskRunnerState(requestId, TaskState.Complete, "");
    }

}
