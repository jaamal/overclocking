package compressionservice.runner.state;

import java.util.UUID;

public interface ITaskStatesStorage {
    UUID registerNew();
    
    TaskStateModel setComplete(UUID requestId);
    TaskStateModel setFailed(UUID requestId, String message);
    
    TaskStateModel getState(UUID requestId);
    TaskStateModel[] getAll();
}
