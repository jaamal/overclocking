package compressionservice.compression.running;

import compressionservice.businessObjects.CompressionRunnerState;
import compressionservice.compression.parameters.ICompressionRunParams;

public interface ICompressionRunner {
    CompressionRunnerState run(ICompressionRunParams runParams);
    boolean isAvailable();
}
