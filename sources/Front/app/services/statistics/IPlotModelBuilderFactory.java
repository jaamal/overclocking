package services.statistics;

import models.statistics.StatisticType;

public interface IPlotModelBuilderFactory
{
	IPlotModelBuidler create(StatisticType xType, StatisticType yType);
}
