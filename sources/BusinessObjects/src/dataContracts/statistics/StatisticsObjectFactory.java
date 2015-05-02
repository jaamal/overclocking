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

    public String getStatisticsObjectId(Map<RunParamKeys, String> runningParameters) {
        return idFactory.getDeterministicID(getKey(runningParameters)).toString();
    }

    public StatisticsObject create(Map<RunParamKeys, String> runningParameters, Map<StatisticKeys, String> statistics) {
        StatisticsObject statisticsObject = new StatisticsObject(runningParameters, statistics);
        statisticsObject.setId(getStatisticsObjectId(runningParameters));
        return statisticsObject;
    }

    private String getKey(Map<RunParamKeys, String> runningParameters) {
        KeyValuePair<RunParamKeys, String>[] array = new KeyValuePair[runningParameters.size()];
        int index = 0;
        for (Map.Entry<RunParamKeys, String> entry : runningParameters.entrySet()) {
            array[index++] = new KeyValuePair<>(entry.getKey(), entry.getValue());
        }
        Arrays.sort(array, new Comparator<KeyValuePair<RunParamKeys, String>>()
        {
            @Override
            public int compare(KeyValuePair<RunParamKeys, String> o1, KeyValuePair<RunParamKeys, String> o2) {
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