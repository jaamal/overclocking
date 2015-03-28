package storage.statistics;

import java.util.HashMap;
import java.util.Map;

import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.StatisticsObject;

public interface IStatisticsRepository {
    boolean exists(String textId, String statisticsId);
    void write(String textId, StatisticsObject statistics);
    boolean contains(String textId, Map<CompressionRunKeys, String> configuration);
    StatisticsObject[] readAll(String textId);
    HashMap<String, StatisticsObject[]> readAll(String[] textIds);

    void remove(String textId, String statisticsId);
}
