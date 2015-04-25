package compressionservice.runner.state;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TaskOperationalLog implements ITaskOperationalLog
{
    private static ConcurrentHashMap<UUID, StringBuilder> opLog = new ConcurrentHashMap<UUID, StringBuilder>();
    
    @Override
    public void append(UUID requestId, String sourceId, String message) {
        opLog.putIfAbsent(requestId, new StringBuilder());
        StringBuilder logBuilder = opLog.get(requestId);
        logBuilder.append(String.format("%s SourceId: %s. %s", LocalDateTime.now(), sourceId, message));
    }

    @Override
    public String get(UUID requestId) {
        if (!opLog.containsKey(requestId)) {
            throw new RuntimeException(String.format("Operational log for requestId %s was not found.", requestId));
        }
        return opLog.get(requestId).toString();
    }

}
