package services.statistics.mapping;

import models.statistics.StatisticType;

public class StatisticAccessorNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = -8983818740726701949L;

	public StatisticAccessorNotFoundException(Class<?> statClass, StatisticType statisticType) {
		super(String.format("Statistic of type %s not found in class %s", statisticType.toString(), statClass.toString()));
	}
}
