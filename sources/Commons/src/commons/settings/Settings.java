package commons.settings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class Settings implements ISettings
{
    private Properties properties;
    private final static String defaultSettingsFile = "application.settings";

    protected Settings(Properties properties)
    {
        this.properties = properties;
    }

    @Override
    public String tryGetString(String setting)
    {
        if (!properties.containsKey(setting))
            return null;
        return properties.getProperty(setting);
    }

    public String getString(String setting)
    {
        if (properties.containsKey(setting))
            return properties.getProperty(setting);
        throw new NoSuchSettingException("No such setting " + setting);
    }

    public int getInt(String setting)
    {
        String settingValue = getString(setting);
        try
        {
            return Integer.parseInt(settingValue);
        }
        catch (NumberFormatException e)
        {
            throw new NoSuchSettingException("Cannot parse to int setting with key " + setting);
        }
    }

    public double getDouble(String setting)
    {
        String settingValue = getString(setting);
        try
        {
            return Double.parseDouble(settingValue);
        }
        catch (NumberFormatException e)
        {
            throw new NoSuchSettingException("Cannot parse to double setting with key " + setting);
        }
    }

    @Override
    public long getLong(String setting)
    {
        String settingValue = getString(setting);
        try
        {
            return Long.parseLong(settingValue);
        }
        catch (NumberFormatException e)
        {
            throw new NoSuchSettingException("Cannot parse to double setting with key " + setting);
        }
    }
    
	@Override
	public <T extends Enum<T>> T getEnum(Class<T> enumClass, String key)
	{
		return Enum.valueOf(enumClass, getString(key));
	}
	
    @Override
    public Path getPath(String key) {
        return Paths.get(getString(key));
    }
	
	public Setting[] toArray() {
	    ArrayList<Setting> result = new ArrayList<>();
	    
	    Enumeration<?> keys = properties.keys();
	    while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = properties.getProperty(key).toString();
            result.add(new Setting(key, value));
        }
	    return result.toArray(new Setting[0]);
	}

    public static ISettings Default()
    {
        return Load(defaultSettingsFile);
    }

    public static ISettings Load(String path)
    {
        Properties properties = new Properties();
        try (Reader reader = new BufferedReader(new FileReader(path));)
        {
            properties.load(reader);
        }
        catch (FileNotFoundException e)
        {
        	throw new SettingsLoadException("File " + path + " was not found.", e);
        }
        catch (IOException e)
        {
        	throw new SettingsLoadException("Unexpected error on file " + path, e);
        }
        return new Settings(properties);
    }
}
