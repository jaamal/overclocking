package compressionservice.businessObjects;

import java.util.UUID;


public interface ICompressionRunnerStateFactory {
    TaskRunnerState createNew();
    TaskRunnerState createFailed(String message);
    TaskRunnerState createFailed(UUID requestId, String message);
    TaskRunnerState createComplete(UUID requestId);
}
