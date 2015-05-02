package dataContracts.statistics;

import java.util.HashMap;
import java.util.Map;

public class Statistics implements IStatistics {
    @Override
    public void putParam(StatisticKeys name, String value) {
        paramsMap.put(name, value);
    }

    @Override
    public void putParam(StatisticKeys name, int value) {
        putParam(name, String.format("%d", value));
    }

    @Override
    public void putParam(StatisticKeys name, long value) {
        putParam(name, String.format("%d", value));
    }

    @Override
    public boolean contains(StatisticKeys name) {
        return paramsMap.containsKey(name);
    }

    @Override
    public String getStrValue(StatisticKeys name) {
        if (!paramsMap.containsKey(name))
            throw new RuntimeException(String.format("Parameter with name %s not found.", name));
        return paramsMap.get(name);
    }

    @Override
    public int getIntValue(StatisticKeys name) {
        String resultStr = getStrValue(name);
        try {
            return Integer.parseInt(resultStr);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Format of parameter %s is not number format. Original format: %s.", name, resultStr));
        }
    }

    @Override
    public long getLongValue(StatisticKeys name) {
        String resultStr = getStrValue(name);
        try {
            return Long.parseLong(resultStr);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Format of parameter %s is not number format. Original format: %s.", name, resultStr));
        }
    }

    @Override
    public Map<StatisticKeys, String> toMap() {
        return paramsMap;
    }

    private final HashMap<StatisticKeys, String> paramsMap = new HashMap<>();
}
