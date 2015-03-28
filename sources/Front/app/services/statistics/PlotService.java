package services.statistics;

import java.util.List;

import models.PlotModel;

public class PlotService implements IPlotService
{
	private IStatisticProvider statisticProvider;
	private IPlotModelBuilderFactory plotModelBuilderFactory;

	public PlotService(IStatisticProvider statisticProvider, IPlotModelBuilderFactory plotModelBuilderFactory) {
		this.statisticProvider = statisticProvider;
		this.plotModelBuilderFactory = plotModelBuilderFactory;
	}
	
	//TODO add plots caching
	@Override
	public PlotModel get(IPlotParameters plotParameters)
	{
		IPlotModelBuidler plotModelBuidler = plotModelBuilderFactory.create(plotParameters.getXType(), plotParameters.getYType());
		List<StatisticElement> statsElements = plotParameters.getStatElements();
		for (StatisticElement element: statsElements) {
			StatisticInfo statisticInfo = statisticProvider.provideStatiticInfo(element, plotParameters.getXType(), plotParameters.getYType());
			plotModelBuidler.append(statisticInfo);
		}
		return plotModelBuidler.build();
	}
}
