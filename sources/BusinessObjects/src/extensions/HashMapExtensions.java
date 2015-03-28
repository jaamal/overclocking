package extensions;

import java.util.HashMap;

public class HashMapExtensions
{
    public static <T extends Enum<T>> T getEnum(HashMap<String, String> map, String key, Class<T> enumClass) {
        if (!map.containsKey(key))
            throw new RuntimeException(String.format("map does not contains key %s.", key));
        return Enum.valueOf(enumClass, map.get(key));
    }

    public static int getInt(HashMap<String, String> map, String key) {
        if (!map.containsKey(key))
            throw new RuntimeException(String.format("map does not contains key %s.", key));
        return Integer.parseInt(map.get(key));
    }
}
