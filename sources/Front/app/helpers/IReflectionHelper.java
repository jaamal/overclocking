package helpers;

public interface IReflectionHelper
{
	Class<?>[] getAllImplementations(Class<?> interfaceClass, String packageName);
}
