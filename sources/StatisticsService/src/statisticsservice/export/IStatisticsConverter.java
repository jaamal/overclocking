package statisticsservice.export;

import dataContracts.statistics.StatisticsObject;

public interface IStatisticsConverter {
    void append(StatisticsObject[] stats);
    String run();
}
