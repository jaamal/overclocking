package services.statistics;

import models.CompressionAlgorithmType;
import models.statistics.StatisticType;
import services.statistics.IPlotParameters;
import services.statistics.IPlotParametersFactory;
import services.statistics.PlotParameters;
import services.statistics.mapping.IStatisticsMapping;

public class PlotParametersFactory implements IPlotParametersFactory
{
	private final IStatisticsMapping statisticsMapping;

	public PlotParametersFactory(IStatisticsMapping statisticsMapping) {
		this.statisticsMapping = statisticsMapping;
	}
	
	@Override
	public IPlotParameters create(StatisticType xType, StatisticType yType, CompressionAlgorithmType... algorithmTypes)
	{
		return new PlotParameters(statisticsMapping, xType, yType, algorithmTypes);
	}

}
