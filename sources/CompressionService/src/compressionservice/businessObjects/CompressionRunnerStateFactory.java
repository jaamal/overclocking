package compressionservice.businessObjects;

import java.util.UUID;

import dataContracts.IIDFactory;

public class CompressionRunnerStateFactory implements ICompressionRunnerStateFactory {

    private IIDFactory idFactory;

    public CompressionRunnerStateFactory(IIDFactory idFactory) {
        this.idFactory = idFactory;
    }
    
    @Override
    public CompressionRunnerState createNew() {
        return new CompressionRunnerState(idFactory.create(), CompressionState.InProgress, "");
    }

    @Override
    public CompressionRunnerState createFailed(String message) {
        return createFailed(idFactory.getEmpty(), message);
    }

    @Override
    public CompressionRunnerState createFailed(UUID requestId, String message) {
        return new CompressionRunnerState(requestId, CompressionState.Failed, message);
    }
    
    @Override
    public CompressionRunnerState createComplete(UUID requestId) {
        return new CompressionRunnerState(requestId, CompressionState.Complete, "");
    }

}
