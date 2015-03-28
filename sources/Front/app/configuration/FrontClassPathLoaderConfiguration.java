package configuration;

import overclocking.jrobocontainer.classpathscanning.IClassPathScannerConfiguration;

public class FrontClassPathLoaderConfiguration implements IClassPathScannerConfiguration
{
	@Override
	public boolean acceptsJar(String arg0)
	{
		return arg0.startsWith("ov.");
	}

	@Override
	public String getClassPaths()
	{
		return System.getProperty("user.dir");
	}
}
