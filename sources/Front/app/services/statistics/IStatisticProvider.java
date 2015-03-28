package services.statistics;

import models.statistics.StatisticType;
import database.DBException;

public interface IStatisticProvider
{
	StatisticInfo provideStatiticInfo(StatisticElement element, StatisticType xType, StatisticType yType) throws DBException, StatisticProviderException;
}
