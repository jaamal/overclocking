package services.statistics;

import java.util.List;

import models.statistics.StatisticType;

public interface IPlotParameters
{
	StatisticType getXType();
	StatisticType getYType();
	List<StatisticElement> getStatElements();
}
