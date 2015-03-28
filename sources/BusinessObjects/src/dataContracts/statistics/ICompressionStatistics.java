package dataContracts.statistics;

import java.util.Map;

public interface ICompressionStatistics {
    void putParam(CompressionStatisticKeys key, String value);
    void putParam(CompressionStatisticKeys key, int value);
    void putParam(CompressionStatisticKeys key, long value);

    boolean contains(CompressionStatisticKeys key);

    String getStrValue(CompressionStatisticKeys key);
    int getIntValue(CompressionStatisticKeys key);
    long getLongValue(CompressionStatisticKeys key);

    Map<CompressionStatisticKeys, String> toMap();
}
