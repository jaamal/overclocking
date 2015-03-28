package services.statistics;

import models.PlotModel;

public interface IPlotService
{
	PlotModel get(IPlotParameters plotParameters);
}
