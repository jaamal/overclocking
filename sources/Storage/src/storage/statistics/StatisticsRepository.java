package storage.statistics;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import serialization.ISerializer;
import storage.KeySpaces;
import storage.cassandraClient.ColumnFamilies;
import storage.cassandraClient.ICassandraConnectionFactory;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;

import dataContracts.statistics.RunParamKeys;
import dataContracts.statistics.StatisticsObject;

public class StatisticsRepository implements IStatisticsRepository {

    private ICassandraConnectionFactory connectionFactory;
    private ISerializer serializer;

    public StatisticsRepository(ISerializer serializer, ICassandraConnectionFactory connectionFactory) {
        this.serializer = serializer;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public boolean exists(String textId, String statisticsId) {
        try {
            Keyspace keyspace = connectionFactory.getKeyspace(KeySpaces.statistics.name());
            ColumnList<String> columns = keyspace.prepareQuery(ColumnFamilies.Statistics)
                    .getRow(textId)
                    .withColumnRange(statisticsId, statisticsId, false, 1)
                    .execute()
                    .getResult();
            return (columns.size() == 1 && columns.getColumnByIndex(0).getName().equals(statisticsId));

        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to read statistics %s for text %s", statisticsId, textId), e);
        }
    }

    @Override
    public void write(String textId, StatisticsObject statistics) {
        try {
            byte[] statsValue = serializer.stringify(statistics).getBytes("UTF8");
            Keyspace keyspace = connectionFactory.getKeyspace(KeySpaces.statistics.name());
            keyspace.prepareColumnMutation(ColumnFamilies.Statistics, textId, statistics.getId())
                    .putValue(statsValue, null)
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to add statistics for text %s", textId), e);
        }
    }

    @Override
    public boolean contains(String textId, Map<RunParamKeys, String> configuration) {
        StatisticsObject[] allStats = readAll(textId);
        for (int i = 0; i < allStats.length; i++) {
            if (areConfiguratonsEqual(configuration, allStats[i].runningParameters))
                return true;
        }
        return false;
    }

    private static boolean areConfiguratonsEqual(Map<RunParamKeys, String> config1, Map<RunParamKeys, String> config2) {
        if (config1.size() != config2.size())
            return false;

        for (Map.Entry<RunParamKeys, String> entry : config1.entrySet()) {
            if (!config2.containsKey(entry.getKey()))
                return false;

            if (!entry.getValue().equalsIgnoreCase(config2.get(entry.getKey())))
                return false;
        }
        return true;
    }

    @Override
    public StatisticsObject[] readAll(String textId) {
        try {
            Keyspace keyspace = connectionFactory.getKeyspace(KeySpaces.statistics.name());
            ColumnList<String> columns = keyspace.prepareQuery(ColumnFamilies.Statistics)
                    .getRow(textId)
                    .execute()
                    .getResult();

            return parseColumns(columns);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to read statistics for text %s", textId), e);
        }
    }

    @Override
    public HashMap<String, StatisticsObject[]> readAll(String[] textIds) {
        try {
            Keyspace keyspace = connectionFactory.getKeyspace(KeySpaces.statistics.name());
            Rows<String, String> rows = keyspace.prepareQuery(ColumnFamilies.Statistics)
                    .getRowSlice(textIds)
                    .execute()
                    .getResult();

            HashMap<String, StatisticsObject[]> result = new HashMap<>();
            for (Row<String, String> row : rows)
                result.put(row.getKey(), parseColumns(row.getColumns()));
            return result;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to read statistics for texts [%s]", textIds), e);
        }
    }

    @Override
    public void remove(String textId, String statisticsId) {
        try {
            Keyspace keyspace = connectionFactory.getKeyspace(KeySpaces.statistics.name());
            keyspace.prepareColumnMutation(ColumnFamilies.Statistics, textId, statisticsId)
                    .deleteColumn()
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to delete statistics %s", statisticsId), e);
        }

    }

    private StatisticsObject[] parseColumns(ColumnList<String> columns) throws UnsupportedEncodingException {
        StatisticsObject[] result = new StatisticsObject[columns.size()];
        for (int i = 0; i < result.length; i++) {
            byte[] bytes = columns.getColumnByIndex(i).getByteArrayValue();
            result[i] = serializer.deserialize(new String(bytes, "UTF8"), StatisticsObject.class);
        }
        return result;
    }
}
