package dataContracts.statistics;

import java.util.HashMap;
import java.util.Map;

public class CompressionStatistics implements ICompressionStatistics {
    @Override
    public void putParam(CompressionStatisticKeys name, String value) {
        paramsMap.put(name, value);
    }

    @Override
    public void putParam(CompressionStatisticKeys name, int value) {
        putParam(name, String.format("%d", value));
    }

    @Override
    public void putParam(CompressionStatisticKeys name, long value) {
        putParam(name, String.format("%d", value));
    }

    @Override
    public boolean contains(CompressionStatisticKeys name) {
        return paramsMap.containsKey(name);
    }

    @Override
    public String getStrValue(CompressionStatisticKeys name) {
        if (!paramsMap.containsKey(name))
            throw new RuntimeException(String.format("Parameter with name %s not found.", name));
        return paramsMap.get(name);
    }

    @Override
    public int getIntValue(CompressionStatisticKeys name) {
        String resultStr = getStrValue(name);
        try {
            return Integer.parseInt(resultStr);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Format of parameter %s is not number format. Original format: %s.", name, resultStr));
        }
    }

    @Override
    public long getLongValue(CompressionStatisticKeys name) {
        String resultStr = getStrValue(name);
        try {
            return Long.parseLong(resultStr);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Format of parameter %s is not number format. Original format: %s.", name, resultStr));
        }
    }

    @Override
    public Map<CompressionStatisticKeys, String> toMap() {
        return paramsMap;
    }

    private final HashMap<CompressionStatisticKeys, String> paramsMap = new HashMap<>();
}
