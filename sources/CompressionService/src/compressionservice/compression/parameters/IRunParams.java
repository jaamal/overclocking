package compressionservice.compression.parameters;

import java.util.Map;

import dataContracts.statistics.CompressionRunKeys;

public interface IRunParams {
    void put(CompressionRunKeys key, String value);
    <T extends Enum<T>> void put(CompressionRunKeys key, Enum<T> value);
    void put(CompressionRunKeys key, int value);

    boolean contains(CompressionRunKeys key);
    
    String get(CompressionRunKeys key) throws RuntimeException;
    int getInt(CompressionRunKeys key) throws RuntimeException;
    <T extends Enum<T>> T getEnum(Class<T> enumClass, CompressionRunKeys key) throws RuntimeException;
    
    default String getOrDefault(CompressionRunKeys key, String defaultValue) {
        try {
            return get(key);
        }
        catch (RuntimeException e) {
            return defaultValue;
        }
    }

    default int getOrDefaultInt(CompressionRunKeys key, int defaultValue) {
        try {
            return getInt(key);
        }
        catch (RuntimeException e) {
            return defaultValue;
        }
    }
    
    default <T extends Enum<T>> T getOrDefaultEnum(Class<T> enumClass, CompressionRunKeys key, T defaultValue) {
        try {
            return getEnum(enumClass, key);
        }
        catch (RuntimeException e) {
            return defaultValue;
        }
    }
    
    Map<CompressionRunKeys, String> toMap();
}
