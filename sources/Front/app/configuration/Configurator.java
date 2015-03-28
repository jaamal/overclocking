package configuration;

import overclocking.jrobocontainer.container.Container;
import overclocking.jrobocontainer.container.IContainer;
import commons.settings.ISettings;

public class Configurator
{
	private static IContainer container;
	
	public static IContainer getContainer() {
		if (container == null)
			container = createContainer();
		return container;
	}
	
	private static synchronized IContainer createContainer() {
		IContainer result = new Container(new FrontClassPathLoaderConfiguration());
		FrontSettings frontSettings = new FrontSettings();
		result.bindInstance(ISettings.class, frontSettings);
		
		return result;
	}
}
