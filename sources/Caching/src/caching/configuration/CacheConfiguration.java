package caching.configuration;

import commons.settings.ISettings;

public class CacheConfiguration implements ICacheConfiguration
{
    private final ISettings applicationSettings;

    public CacheConfiguration(ISettings applicationSettings)
    {
        this.applicationSettings = applicationSettings;
    }

    @Override
    public int getCacheLineCount()
    {
        return applicationSettings.getInt("CacheLineCount");
    }

    @Override
    public int getCacheLineLength()
    {
        return applicationSettings.getInt("CacheLineLength");
    }
}
