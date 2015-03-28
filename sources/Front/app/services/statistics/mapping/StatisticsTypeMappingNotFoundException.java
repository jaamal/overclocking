package services.statistics.mapping;

import models.statistics.StatisticType;

public class StatisticsTypeMappingNotFoundException extends RuntimeException
{

	private static final long serialVersionUID = 1L;
	
	public StatisticsTypeMappingNotFoundException(StatisticType statisticType, int cacheCount) {
		super(String.format("Mapping for statistics type %s not found. cacheCount: %s", statisticType.toString(), cacheCount));
	}
}
