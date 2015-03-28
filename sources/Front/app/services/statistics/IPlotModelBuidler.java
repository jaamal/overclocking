package services.statistics;

import models.PlotModel;

public interface IPlotModelBuidler
{
	void append(StatisticInfo statisticInfo);
	PlotModel build();
}
