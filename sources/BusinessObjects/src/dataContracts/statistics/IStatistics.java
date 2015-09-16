package dataContracts.statistics;

import java.util.Map;

//TODO rename to metrics
public interface IStatistics {
    void putParam(StatisticKeys key, String value);
    void putParam(StatisticKeys key, int value);
    void putParam(StatisticKeys key, long value);

    boolean contains(StatisticKeys key);

    String getStrValue(StatisticKeys key);
    int getIntValue(StatisticKeys key);
    long getLongValue(StatisticKeys key);

    Map<StatisticKeys, String> toMap();
}
