package configuration;

import commons.settings.Settings;
import play.Play;

public class FrontSettings extends Settings
{
    public FrontSettings()
    {
        super(Play.configuration);
    }
}
