package compressionservice.businessObjects;

import java.util.UUID;


public interface ICompressionRunnerStateFactory {
    CompressionRunnerState createNew();
    CompressionRunnerState createFailed(String message);
    CompressionRunnerState createFailed(UUID requestId, String message);
    CompressionRunnerState createComplete(UUID requestId);
}
