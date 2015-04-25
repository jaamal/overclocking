package compressionservice.runner.state;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRunnerStatesStorage implements ITasksRunnerStatesStorage {

    private static ConcurrentHashMap<UUID, TaskRunnerState> statesMap = new ConcurrentHashMap<UUID, TaskRunnerState>();
    private ICompressionRunnerStateFactory compressionRunnerStateFactory;

    public TaskRunnerStatesStorage(ICompressionRunnerStateFactory compressionRunnerStateFactory) {
        this.compressionRunnerStateFactory = compressionRunnerStateFactory;
    }
    
    @Override
    public TaskRunnerState registerNew() {
        TaskRunnerState result = compressionRunnerStateFactory.createNew();
        statesMap.put(result.requestId, result);
        return result;
    }

    @Override
    public TaskRunnerState setComplete(UUID requestId) {
        TaskRunnerState result = compressionRunnerStateFactory.createComplete(requestId);
        statesMap.put(result.requestId, result);
        return result;
    }

    @Override
    public TaskRunnerState setFailed(UUID requestId, String message) {
        TaskRunnerState result = compressionRunnerStateFactory.createFailed(requestId, message);
        statesMap.put(result.requestId, result);
        return result;
    }

    @Override
    public TaskRunnerState getState(UUID requestId) {
        if (!statesMap.containsKey(requestId))
            throw new RuntimeException();
        return statesMap.get(requestId);
    }

    @Override
    public TaskRunnerState[] getAll() {
        return statesMap.values().toArray(new TaskRunnerState[0]);
    }

}