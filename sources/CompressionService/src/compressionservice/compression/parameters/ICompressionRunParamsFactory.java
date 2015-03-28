package compressionservice.compression.parameters;

import java.util.Map;

public interface ICompressionRunParamsFactory {
    ICompressionRunParams create(Map<String, String> parameters);
}
