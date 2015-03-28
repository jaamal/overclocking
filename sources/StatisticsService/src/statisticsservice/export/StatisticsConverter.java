package statisticsservice.export;

import java.util.ArrayList;
import java.util.List;

import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class StatisticsConverter implements IStatisticsConverter {
    
    List<StatisticsObject> stats = new ArrayList<StatisticsObject>();
    
    CompressionRunKeys[] runKeys = CompressionRunKeys.values();
    CompressionStatisticKeys[] statsKeys = CompressionStatisticKeys.values();
    
    @Override
    public void append(StatisticsObject[] stats) {
        for (StatisticsObject statisticsObject : stats) {
            this.stats.add(statisticsObject);
        }
    }

    @Override
    public String run() {
        StringBuilder builder = new StringBuilder();
        
        for (CompressionRunKeys runKey : runKeys) {
            builder.append(runKey);
            builder.append(";");
        }
        for (CompressionStatisticKeys statKey : statsKeys) {
            builder.append(statKey);
            builder.append(";");
        }
        builder.append("\r\n");
        
        for (StatisticsObject statisticsObject : stats) {
            for (CompressionRunKeys runKey : runKeys) {
                if (statisticsObject.runningParameters.containsKey(runKey))
                    builder.append(statisticsObject.runningParameters.get(runKey));
                builder.append(";");
            }
            for (CompressionStatisticKeys statKey : statsKeys) {
                if (statisticsObject.statistics.containsKey(statKey))
                    builder.append(statisticsObject.statistics.get(statKey));
                builder.append(";");
            }
            builder.append("\r\n");
        }
        
        return builder.toString();
    }

}
