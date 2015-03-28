package services.statistics;

import models.CompressionAlgorithmType;
import models.statistics.StatisticType;

public interface IPlotParametersFactory
{
	IPlotParameters create(StatisticType xType, StatisticType yType, CompressionAlgorithmType... algorithmTypes);
}
