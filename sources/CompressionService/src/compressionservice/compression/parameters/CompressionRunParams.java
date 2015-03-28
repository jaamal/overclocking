package compressionservice.compression.parameters;

import java.util.HashMap;
import java.util.Map;

import dataContracts.statistics.CompressionRunKeys;

public class CompressionRunParams implements ICompressionRunParams {

    private Map<CompressionRunKeys, String> paramsMap;

    public CompressionRunParams() {
        this(new HashMap<CompressionRunKeys, String>());
    }

    public CompressionRunParams(Map<CompressionRunKeys, String> paramsMap) {
        this.paramsMap = paramsMap;
    }
    
    @Override
    public void putParam(CompressionRunKeys name, String value) {
        paramsMap.put(name, value);
    }

    @Override
    public <T extends Enum<T>> void putParam(CompressionRunKeys name, Enum<T> value) {
        putParam(name, value.name());
    }

    @Override
    public void putParam(CompressionRunKeys name, int value) {
        putParam(name, Integer.toString(value));
    }

    @Override
    public boolean contains(CompressionRunKeys name) {
        return paramsMap.containsKey(name);
    }

    @Override
    public String getStrValue(CompressionRunKeys name) {
        if (!paramsMap.containsKey(name))
            throw new RuntimeException(String.format("Parameter with name %s not found.", name));
        return paramsMap.get(name);
    }

    @Override
    public int getIntValue(CompressionRunKeys name) {
        String resultStr = getStrValue(name);
        try {
            return Integer.parseInt(resultStr);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Format of parameter %s is not number format. Original format: %s.", name, resultStr));
        }
    }

    @Override
    public <T extends Enum<T>> T getEnumValue(Class<T> enumClass, CompressionRunKeys key) {
        String resultStr = getStrValue(key);
        return Enum.valueOf(enumClass, resultStr);
    }

    @Override
    public Map<CompressionRunKeys, String> toMap() {
        return paramsMap;
    }

}
