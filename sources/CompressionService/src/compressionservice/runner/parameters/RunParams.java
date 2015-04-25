package compressionservice.runner.parameters;

import java.util.HashMap;
import java.util.Map;

import dataContracts.statistics.CompressionRunKeys;

public class RunParams implements IRunParams {

    private Map<CompressionRunKeys, String> paramsMap;

    public RunParams() {
        this(new HashMap<CompressionRunKeys, String>());
    }

    public RunParams(Map<CompressionRunKeys, String> paramsMap) {
        this.paramsMap = paramsMap;
    }
    
    @Override
    public void put(CompressionRunKeys key, String value) {
        paramsMap.put(key, value);
    }

    @Override
    public <T extends Enum<T>> void put(CompressionRunKeys key, Enum<T> value) {
        put(key, value.name());
    }

    @Override
    public void put(CompressionRunKeys key, int value) {
        put(key, Integer.toString(value));
    }

    @Override
    public boolean contains(CompressionRunKeys key) {
        return paramsMap.containsKey(key);
    }
    
    @Override
    public String get(CompressionRunKeys key) throws RuntimeException {
        if (!paramsMap.containsKey(key))
            throw new RuntimeException(String.format("Parameter with name %s not found.", key));
        return paramsMap.get(key);
    }

    @Override
    public int getInt(CompressionRunKeys key) throws RuntimeException {
        String resultStr = get(key);
        try {
            return Integer.parseInt(resultStr);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Format of parameter %s is not number format. Original format: %s.", key, resultStr));
        }
    }

    @Override
    public <T extends Enum<T>> T getEnum(Class<T> enumClass, CompressionRunKeys key) throws RuntimeException {
        String resultStr = get(key);
        return Enum.valueOf(enumClass, resultStr);
    }

    @Override
    public Map<CompressionRunKeys, String> toMap() {
        return paramsMap;
    }
}
