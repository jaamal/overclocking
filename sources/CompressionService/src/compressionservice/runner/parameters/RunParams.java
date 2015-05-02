package compressionservice.runner.parameters;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import serialization.ISerializer;
import dataContracts.IIDFactory;
import dataContracts.statistics.RunParamKeys;

public class RunParams implements IRunParams {

    private IIDFactory idFactory;
    private ISerializer serializer;
    private Map<RunParamKeys, String> paramsMap;

    public RunParams(IIDFactory idFactory, ISerializer serializer) {
        this(idFactory, serializer, new HashMap<RunParamKeys, String>());
    }

    public RunParams(
            IIDFactory idFactory, 
            ISerializer serializer,
            Map<RunParamKeys, String> paramsMap) {
        this.idFactory = idFactory;
        this.serializer = serializer;
        this.paramsMap = paramsMap;
    }
    
    @Override
    public void put(RunParamKeys key, String value) {
        paramsMap.put(key, value);
    }

    @Override
    public <T extends Enum<T>> void put(RunParamKeys key, Enum<T> value) {
        put(key, value.name());
    }

    @Override
    public void put(RunParamKeys key, int value) {
        put(key, Integer.toString(value));
    }

    @Override
    public boolean contains(RunParamKeys key) {
        return paramsMap.containsKey(key);
    }
    
    @Override
    public String get(RunParamKeys key) throws RuntimeException {
        if (!paramsMap.containsKey(key))
            throw new RuntimeException(String.format("Parameter with name %s not found.", key));
        return paramsMap.get(key);
    }

    @Override
    public int getInt(RunParamKeys key) throws RuntimeException {
        String resultStr = get(key);
        try {
            return Integer.parseInt(resultStr);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Format of parameter %s is not number format. Original format: %s.", key, resultStr));
        }
    }

    @Override
    public <T extends Enum<T>> T getEnum(Class<T> enumClass, RunParamKeys key) throws RuntimeException {
        String resultStr = get(key);
        return Enum.valueOf(enumClass, resultStr);
    }

    @Override
    public Map<RunParamKeys, String> toMap() {
        return paramsMap;
    }

    @Override
    public String getHashId() {
        return idFactory.getDeterministicID(getHash()).toString();
    }
    
    private String getHash() {
        KeyValuePair<RunParamKeys, String>[] array = new KeyValuePair[paramsMap.size()];
        int index = 0;
        for (Map.Entry<RunParamKeys, String> entry : paramsMap.entrySet()) {
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
