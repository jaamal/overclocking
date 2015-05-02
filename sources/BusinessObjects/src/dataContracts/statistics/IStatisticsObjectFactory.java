package dataContracts.statistics;

import java.util.Map;

public interface IStatisticsObjectFactory {
    StatisticsObject create(String resultId, Map<RunParamKeys, String> runParams, Map<StatisticKeys, String> statistics);
}
