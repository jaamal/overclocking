package services.statistics.mapping;

import helpers.IReflectionHelper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import models.CompressionAlgorithmType;
import models.statistics.ICompressionStats;
import models.statistics.StatisticType;
import services.statistics.AlgorithmType;
import services.statistics.StatisticAccessor;
import services.statistics.StatisticElement;

public class StatisticsMapping implements IStatisticsMapping
{
	private final String statisticPackage = "models.statistics";
	private IReflectionHelper reflectionHelper;
	private HashMap<CompressionAlgorithmType, Class<?>> algorithmTypeMap = new HashMap<CompressionAlgorithmType, Class<?>>();
	private HashMap<StatisticType, List<StatisticElement>> statisticsTypeMap = new HashMap<StatisticType, List<StatisticElement>>();
	
	public StatisticsMapping(IReflectionHelper reflectionHelper) {
		this.reflectionHelper = reflectionHelper;
		initialize();
	}
	
	@Override
	public Class<?> getAlgorithmClass(CompressionAlgorithmType algorithmType) throws AlgorithmTypeMappingNotFoundException
	{
		if (!algorithmTypeMap.containsKey(algorithmType))
			throw new AlgorithmTypeMappingNotFoundException(algorithmType);
		return algorithmTypeMap.get(algorithmType);
	}
	
	
	private void initialize() {
		Class<?>[] impls = reflectionHelper.getAllImplementations(ICompressionStats.class, statisticPackage);
		for (Class<?> impl : impls) {
			CompressionAlgorithmType algorithmType = impl.getAnnotation(AlgorithmType.class).algorithmType();
			algorithmTypeMap.put(algorithmType, impl);
		
			for (Method method : impl.getMethods()){
				StatisticAccessor annotation = method.getAnnotation(StatisticAccessor.class);
				if (annotation == null) continue;
				if (!statisticsTypeMap.containsKey(annotation.statisticType())) 
					statisticsTypeMap.put(annotation.statisticType(), new ArrayList<StatisticElement>());
				statisticsTypeMap.get(annotation.statisticType()).add(new StatisticElement(impl, algorithmType));
			}
		}
	}

	@Override
	public List<StatisticElement> getClasses(StatisticType statisticType) throws StatisticsTypeMappingNotFoundException 
	{
		
		if (!statisticsTypeMap.containsKey(statisticType)) 
			throw new StatisticsTypeMappingNotFoundException(statisticType, statisticsTypeMap.size());
		return statisticsTypeMap.get(statisticType);
	}

	private HashMap<Integer, Method> methodsMap = new HashMap<Integer, Method>();
	
	@Override
	public Method getMethod(Class<?> statsClass, StatisticType statisticType) throws StatisticAccessorNotFoundException
	{
		int methodKey = statsClass.hashCode() + statisticType.hashCode();
		if (methodsMap.containsKey(methodKey))
			return methodsMap.get(methodKey);
		
		Method[] declaredMethods = statsClass.getDeclaredMethods();
		for (Method method : declaredMethods) {
			StatisticAccessor accessor = method.getAnnotation(StatisticAccessor.class);
			if (accessor == null)
				continue;
			
			if (accessor.statisticType() == statisticType){
				methodsMap.put(methodKey, method);
				return method;
			}	
		}
		throw new StatisticAccessorNotFoundException(statsClass, statisticType);
	}
}
