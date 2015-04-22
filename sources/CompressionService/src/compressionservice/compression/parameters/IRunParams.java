package compressionservice.compression.parameters;

import java.util.Map;

import dataContracts.statistics.CompressionRunKeys;

public interface IRunParams {
    void put(CompressionRunKeys key, String value);
    <T extends Enum<T>> void put(CompressionRunKeys key, Enum<T> value);
    void put(CompressionRunKeys key, int value);

    boolean contains(CompressionRunKeys key);
    
    String get(CompressionRunKeys key) throws RuntimeException;
    int getInt(CompressionRunKeys key);
    <T extends Enum<T>> T getEnum(Class<T> enumClass, CompressionRunKeys key);
    
    Map<CompressionRunKeys, String> toMap();
}
