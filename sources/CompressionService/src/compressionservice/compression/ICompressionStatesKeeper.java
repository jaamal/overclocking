package compressionservice.compression;

import java.util.UUID;

import compressionservice.businessObjects.CompressionRunnerState;

public interface ICompressionStatesKeeper {
    CompressionRunnerState registerNew();
    CompressionRunnerState setComplete(UUID requestId);
    CompressionRunnerState setFailed(UUID requestId, String message);
    CompressionRunnerState getState(UUID requestId);
    CompressionRunnerState[] getAll();
}
