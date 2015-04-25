package compressionservice.runner.state;

import java.util.UUID;

public interface ITaskOperationalLog
{
    void append(UUID requestId, String sourceId, String message);
    String get(UUID requestId);
}
