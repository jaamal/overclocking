package compressionservice.compression.running;

import compressionservice.businessObjects.CompressionRunnerState;
import compressionservice.compression.parameters.IRunParams;

public interface ICompressionRunner {
    CompressionRunnerState run(IRunParams runParams);
    boolean isAvailable();
}
