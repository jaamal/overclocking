package dataContracts.statistics;

import java.util.Map;

import dataContracts.BusinessObject;

public class StatisticsObject extends BusinessObject {
    public final Map<StatisticKeys, String> statistics;
    public final Map<RunParamKeys, String> runningParameters;

    public StatisticsObject(Map<RunParamKeys, String> runningParameters, Map<StatisticKeys, String> statistics) {
        this.runningParameters = runningParameters;
        this.statistics = statistics;
    }
}
