package statisticsservice.export;

import java.util.ArrayList;
import java.util.List;

import dataContracts.statistics.RunParamKeys;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class StatisticsConverter implements IStatisticsConverter {
    
    List<StatisticsObject> stats = new ArrayList<StatisticsObject>();
    
    RunParamKeys[] runKeys = RunParamKeys.values();
    StatisticKeys[] statsKeys = StatisticKeys.values();
    
    @Override
    public void append(StatisticsObject[] stats) {
        for (StatisticsObject statisticsObject : stats) {
            this.stats.add(statisticsObject);
        }
    }

    @Override
    public String run() {
        StringBuilder builder = new StringBuilder();
        
        for (RunParamKeys runKey : runKeys) {
            builder.append(runKey);
            builder.append(";");
        }
        for (StatisticKeys statKey : statsKeys) {
            builder.append(statKey);
            builder.append(";");
        }
        builder.append("\r\n");
        
        for (StatisticsObject statisticsObject : stats) {
            for (RunParamKeys runKey : runKeys) {
                if (statisticsObject.runningParameters.containsKey(runKey))
                    builder.append(statisticsObject.runningParameters.get(runKey));
                builder.append(";");
            }
            for (StatisticKeys statKey : statsKeys) {
                if (statisticsObject.statistics.containsKey(statKey))
                    builder.append(statisticsObject.statistics.get(statKey));
                builder.append(";");
            }
            builder.append("\r\n");
        }
        
        return builder.toString();
    }

}
