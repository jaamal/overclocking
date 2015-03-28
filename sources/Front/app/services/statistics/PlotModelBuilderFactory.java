package services.statistics;

import models.statistics.StatisticType;
import services.statistics.scaling.ICompositeStatisticScaler;
import factories.IPlotFactory;

public class PlotModelBuilderFactory implements IPlotModelBuilderFactory
{
	private final IPlotFactory plotFacotry;
	private final ICompositeStatisticScaler compositeStatisticScaler;

	public PlotModelBuilderFactory(IPlotFactory plotFacotry, ICompositeStatisticScaler compositeStatisticScaler) {
		this.plotFacotry = plotFacotry;
		this.compositeStatisticScaler = compositeStatisticScaler;
		
	}
	
	@Override
	public IPlotModelBuidler create(StatisticType xType, StatisticType yType)
	{
		return new PlotModelBuilder(plotFacotry, compositeStatisticScaler, xType, yType);
	}
}
