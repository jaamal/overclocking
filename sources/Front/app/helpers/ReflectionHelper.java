package helpers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import play.Logger;

public class ReflectionHelper implements IReflectionHelper
{
	@Override
	public Class<?>[] getAllImplementations(Class<?> interfaceClass, String packageName)
	{
		try {
			ArrayList<Class<?>> result = new ArrayList<Class<?>>();
			Iterable<Class<?>> classes = getClasses(packageName);
			for (Class<?> classObj : classes) {
				Class<?>[] implInterfaces = classObj.getInterfaces();
				for (Class<?> implInterface : implInterfaces) {
					if (implInterface.equals(interfaceClass)){
						result.add(classObj);
						break;
					}
				}
			}
			return result.toArray(new Class<?>[0]);
		}
		catch (Exception e) {
			Logger.error("Fail to gather all implementations of some class.", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private Iterable<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException
	{
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    String path = packageName.replace('.', '/');
	    
	    Enumeration<URL> resources = classLoader.getResources(path);
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements())
	    {
	        URL resource = resources.nextElement();
	        
	        //TODO: it is a cheat
	        if (resource.getPath().contains("/app/")) {
	        	resource = new URL("file:/" + resource.getPath().replace("/app/", "/eclipse/classes/"));
	        }
	        dirs.add(new File(resource.getFile()));
	    }
	    List<Class<?>> result = new ArrayList<Class<?>>();
	    for (File directory : dirs)
	        result.addAll(findClasses(directory, packageName));
	    return result;
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 * @param directory The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException
	{
	    List<Class<?>> result = new ArrayList<Class<?>>();
	    if (!directory.exists())
	        return result;
	    
	    File[] files = directory.listFiles();
	    for (File file : files)
	    {
	        if (file.isDirectory())
	            result.addAll(findClasses(file, packageName + "." + file.getName()));
	        else if (file.getName().endsWith(".class"))
	            result.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
	    }
	    return result;
	}

}
