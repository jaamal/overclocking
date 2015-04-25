package compressionservice.runner.state;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TaskStatesStorage implements ITaskStatesStorage {

    private static ConcurrentHashMap<UUID, TaskStateModel> statesMap = new ConcurrentHashMap<UUID, TaskStateModel>();
    private ITaskRunnerStateFactory taskRunnerStateFactory;

    public TaskStatesStorage(ITaskRunnerStateFactory taskRunnerStateFactory) {
        this.taskRunnerStateFactory = taskRunnerStateFactory;
    }
    
    @Override
    public UUID registerNew() {
        TaskStateModel state = taskRunnerStateFactory.inProgress();
        statesMap.put(state.requestId, state);
        return state.requestId;
    }

    @Override
    public TaskStateModel setComplete(UUID requestId) {
        TaskStateModel state = taskRunnerStateFactory.complete(requestId);
        statesMap.put(state.requestId, state);
        return state;
    }

    @Override
    public TaskStateModel setFailed(UUID requestId, String message) {
        TaskStateModel state = taskRunnerStateFactory.failed(requestId, message);
        statesMap.put(state.requestId, state);
        return state;
    }

    @Override
    public TaskStateModel getState(UUID requestId) {
        if (!statesMap.containsKey(requestId))
            throw new RuntimeException(String.format("State with id %s was not found.", requestId));
        return statesMap.get(requestId);
    }

    @Override
    public TaskStateModel[] getAll() {
        return statesMap.values().toArray(new TaskStateModel[0]);
    }

}