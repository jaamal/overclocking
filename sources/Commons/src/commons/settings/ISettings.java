package commons.settings;

import java.nio.file.Path;

public interface ISettings
{
    /**
     * @param key
     * @return null, if key does not exists
     */
    String tryGetString(String key);

    String getString(String key);

    int getInt(String key);

    double getDouble(String key);

    long getLong(String key);
    
    <T extends Enum<T>> T getEnum(Class<T> enumClass, String key);
    
    Path getPath(String key); 
    
    Setting[] toArray();
}
