package dataContracts.statistics;

import java.util.Map;

public interface IStatisticsObjectFactory {
    String getStatisticsObjectId(Map<RunParamKeys, String> runningParameters);

    StatisticsObject create(Map<RunParamKeys, String> runningParameters, Map<StatisticKeys, String> statistics);
}
