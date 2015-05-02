package dataContracts.statistics;

import java.util.Map;

public class StatisticsObjectFactory implements IStatisticsObjectFactory
{
    public StatisticsObject create(String resultId, Map<RunParamKeys, String> runningParameters, Map<StatisticKeys, String> statistics) {
        StatisticsObject statisticsObject = new StatisticsObject(runningParameters, statistics);
        statisticsObject.setId(resultId);
        return statisticsObject;
    }
}