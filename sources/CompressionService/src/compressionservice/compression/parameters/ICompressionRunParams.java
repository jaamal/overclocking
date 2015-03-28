package compressionservice.compression.parameters;

import java.util.Map;

import dataContracts.statistics.CompressionRunKeys;

public interface ICompressionRunParams {
    void putParam(CompressionRunKeys key, String value);
    <T extends Enum<T>> void putParam(CompressionRunKeys key, Enum<T> value);
    void putParam(CompressionRunKeys key, int value);

    boolean contains(CompressionRunKeys key);
    
    String getStrValue(CompressionRunKeys key);
    int getIntValue(CompressionRunKeys key);
    <T extends Enum<T>> T getEnumValue(Class<T> enumClass, CompressionRunKeys key);
    
    Map<CompressionRunKeys, String> toMap();
}
