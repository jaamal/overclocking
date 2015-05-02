package compressionservice.runner.parameters;

import java.util.Map;

import dataContracts.statistics.RunParamKeys;

public interface IRunParams {
    void put(RunParamKeys key, String value);
    <T extends Enum<T>> void put(RunParamKeys key, Enum<T> value);
    void put(RunParamKeys key, int value);

    boolean contains(RunParamKeys key);
    
    String get(RunParamKeys key) throws RuntimeException;
    int getInt(RunParamKeys key) throws RuntimeException;
    <T extends Enum<T>> T getEnum(Class<T> enumClass, RunParamKeys key) throws RuntimeException;
    
    default String getOrDefault(RunParamKeys key, String defaultValue) {
        try {
            return get(key);
        }
        catch (RuntimeException e) {
            return defaultValue;
        }
    }

    default int getOrDefaultInt(RunParamKeys key, int defaultValue) {
        try {
            return getInt(key);
        }
        catch (RuntimeException e) {
            return defaultValue;
        }
    }
    
    default <T extends Enum<T>> T getOrDefaultEnum(Class<T> enumClass, RunParamKeys key, T defaultValue) {
        try {
            return getEnum(enumClass, key);
        }
        catch (RuntimeException e) {
            return defaultValue;
        }
    }
    
    Map<RunParamKeys, String> toMap();
}
