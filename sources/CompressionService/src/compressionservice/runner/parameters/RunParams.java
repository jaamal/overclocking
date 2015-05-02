package compressionservice.runner.parameters;

import java.util.HashMap;
import java.util.Map;

import dataContracts.statistics.RunParamKeys;

public class RunParams implements IRunParams {

    private Map<RunParamKeys, String> paramsMap;

    public RunParams() {
        this(new HashMap<RunParamKeys, String>());
    }

    public RunParams(Map<RunParamKeys, String> paramsMap) {
        this.paramsMap = paramsMap;
    }
    
    @Override
    public void put(RunParamKeys key, String value) {
        paramsMap.put(key, value);
    }

    @Override
    public <T extends Enum<T>> void put(RunParamKeys key, Enum<T> value) {
        put(key, value.name());
    }

    @Override
    public void put(RunParamKeys key, int value) {
        put(key, Integer.toString(value));
    }

    @Override
    public boolean contains(RunParamKeys key) {
        return paramsMap.containsKey(key);
    }
    
    @Override
    public String get(RunParamKeys key) throws RuntimeException {
        if (!paramsMap.containsKey(key))
            throw new RuntimeException(String.format("Parameter with name %s not found.", key));
        return paramsMap.get(key);
    }

    @Override
    public int getInt(RunParamKeys key) throws RuntimeException {
        String resultStr = get(key);
        try {
            return Integer.parseInt(resultStr);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Format of parameter %s is not number format. Original format: %s.", key, resultStr));
        }
    }

    @Override
    public <T extends Enum<T>> T getEnum(Class<T> enumClass, RunParamKeys key) throws RuntimeException {
        String resultStr = get(key);
        return Enum.valueOf(enumClass, resultStr);
    }

    @Override
    public Map<RunParamKeys, String> toMap() {
        return paramsMap;
    }
}
