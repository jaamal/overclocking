package compressionservice.runner.parameters;

import java.util.Map;

public interface IRunParamsFactory {
    IRunParams create(Map<String, String> parameters);
}
