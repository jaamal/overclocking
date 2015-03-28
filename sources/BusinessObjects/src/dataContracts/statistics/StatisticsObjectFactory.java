package dataContracts.statistics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import serialization.ISerializer;
import dataContracts.IIDFactory;

public class StatisticsObjectFactory implements IStatisticsObjectFactory
{
    private IIDFactory idFactory;
    private ISerializer serializer;

    public StatisticsObjectFactory(IIDFactory idFactory, ISerializer serializer)
    {
        this.idFactory = idFactory;
        this.serializer = serializer;
    }

    public String getStatisticsObjectId(Map<CompressionRunKeys, String> runningParameters) {
        return idFactory.getDeterministicID(getKey(runningParameters)).toString();
    }

    public StatisticsObject create(Map<CompressionRunKeys, String> runningParameters, Map<CompressionStatisticKeys, String> statistics) {
        StatisticsObject statisticsObject = new StatisticsObject(runningParameters, statistics);
        statisticsObject.setId(getStatisticsObjectId(runningParameters));
        return statisticsObject;
    }

    private String getKey(Map<CompressionRunKeys, String> runningParameters) {
        KeyValuePair<CompressionRunKeys, String>[] array = new KeyValuePair[runningParameters.size()];
        int index = 0;
        for (Map.Entry<CompressionRunKeys, String> entry : runningParameters.entrySet()) {
            array[index++] = new KeyValuePair<>(entry.getKey(), entry.getValue());
        }
        Arrays.sort(array, new Comparator<KeyValuePair<CompressionRunKeys, String>>()
        {
            @Override
            public int compare(KeyValuePair<CompressionRunKeys, String> o1, KeyValuePair<CompressionRunKeys, String> o2) {
                return o1.key.compareTo(o2.key);
            }
        });
        return serializer.stringify(array);
    }

    private class KeyValuePair<TKey, TValue>
    {
        public final TKey key;
        public final TValue value;

        private KeyValuePair(TKey key, TValue value)
        {
            this.key = key;
            this.value = value;
        }
    }
}