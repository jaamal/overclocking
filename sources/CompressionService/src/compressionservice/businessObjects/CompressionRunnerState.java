package compressionservice.businessObjects;

import java.util.UUID;

public class CompressionRunnerState {
    public final UUID requestId;
    public final CompressionState state;
    public final String message;
    
    public CompressionRunnerState(UUID requestId, CompressionState state, String message) {
        this.requestId = requestId;
        this.state = state;
        this.message = message;
    }
}
