package services.statistics;

import handlers.IStatisticsHandler;

import java.util.Collection;

import models.statistics.StatisticType;
import services.statistics.mapping.IStatisticsMapping;
import database.DBException;

public class StatisticProvider implements IStatisticProvider
{
	private IStatisticHandlerFactory statisticHandlerFactory;
	private IStatisticsMapping statisticsMapping;

	public StatisticProvider(IStatisticsMapping statisticsMapping, IStatisticHandlerFactory statisticHandlerFactory) {
		this.statisticsMapping = statisticsMapping;
		this.statisticHandlerFactory = statisticHandlerFactory;
	}
	
	@Override
	public StatisticInfo provideStatiticInfo(StatisticElement element, StatisticType xType, StatisticType yType) throws DBException, StatisticProviderException
	{
		IStatisticsHandler statsHandler = statisticHandlerFactory.create();
		Collection<?> statistic = statsHandler.getStatistics(element.statisticClass);
		
		Object[] xValues = new Object[statistic.size()];
		Object[] yValues = new Object[statistic.size()];
		int index = 0;
		for (Object item : statistic) {
			try {
				xValues[index] = statisticsMapping.getMethod(element.statisticClass, xType).invoke(item);
				yValues[index] = statisticsMapping.getMethod(element.statisticClass, yType).invoke(item);
				index++;
			}
			catch (Exception e) {
				throw new StatisticProviderException("Unable to get statistic info from item. " + e.getMessage(), e);
			}
		}
		return new StatisticInfo(element.algorithmType, xValues, yValues);
	}

}
