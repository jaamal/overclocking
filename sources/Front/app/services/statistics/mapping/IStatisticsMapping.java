package services.statistics.mapping;

import java.lang.reflect.Method;
import java.util.List;
import services.statistics.StatisticElement;
import models.CompressionAlgorithmType;
import models.statistics.StatisticType;

public interface IStatisticsMapping
{

	Class<?> getAlgorithmClass(CompressionAlgorithmType algorithmType) throws AlgorithmTypeMappingNotFoundException;

	List<StatisticElement> getClasses(StatisticType statisticType);
	
	Method getMethod(Class<?> statsClass, StatisticType statisticType) throws StatisticAccessorNotFoundException;
	
}
