package dataContracts.statistics;

import java.util.Map;

import dataContracts.BusinessObject;

public class StatisticsObject extends BusinessObject {
    public final Map<CompressionStatisticKeys, String> statistics;
    public final Map<CompressionRunKeys, String> runningParameters;

    public StatisticsObject(Map<CompressionRunKeys, String> runningParameters, Map<CompressionStatisticKeys, String> statistics) {
        this.runningParameters = runningParameters;
        this.statistics = statistics;
    }
}
