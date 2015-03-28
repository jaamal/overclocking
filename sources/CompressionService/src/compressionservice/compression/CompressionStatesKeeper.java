package compressionservice.compression;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import compressionservice.businessObjects.CompressionRunnerState;
import compressionservice.businessObjects.ICompressionRunnerStateFactory;

public class CompressionStatesKeeper implements ICompressionStatesKeeper {

    private static ConcurrentHashMap<UUID, CompressionRunnerState> statesMap = new ConcurrentHashMap<UUID, CompressionRunnerState>();
    private ICompressionRunnerStateFactory compressionRunnerStateFactory;

    public CompressionStatesKeeper(ICompressionRunnerStateFactory compressionRunnerStateFactory) {
        this.compressionRunnerStateFactory = compressionRunnerStateFactory;
    }
    
    @Override
    public CompressionRunnerState registerNew() {
        CompressionRunnerState result = compressionRunnerStateFactory.createNew();
        statesMap.put(result.requestId, result);
        return result;
    }

    @Override
    public CompressionRunnerState setComplete(UUID requestId) {
        CompressionRunnerState result = compressionRunnerStateFactory.createComplete(requestId);
        statesMap.put(result.requestId, result);
        return result;
    }

    @Override
    public CompressionRunnerState setFailed(UUID requestId, String message) {
        CompressionRunnerState result = compressionRunnerStateFactory.createFailed(requestId, message);
        statesMap.put(result.requestId, result);
        return result;
    }

    @Override
    public CompressionRunnerState getState(UUID requestId) {
        if (!statesMap.containsKey(requestId))
            throw new RuntimeException();
        return statesMap.get(requestId);
    }

    @Override
    public CompressionRunnerState[] getAll() {
        return statesMap.values().toArray(new CompressionRunnerState[0]);
    }

}