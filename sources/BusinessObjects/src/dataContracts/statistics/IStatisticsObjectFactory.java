package dataContracts.statistics;

import java.util.Map;

public interface IStatisticsObjectFactory {
    String getStatisticsObjectId(Map<CompressionRunKeys, String> runningParameters);

    StatisticsObject create(Map<CompressionRunKeys, String> runningParameters, Map<CompressionStatisticKeys, String> statistics);
}
