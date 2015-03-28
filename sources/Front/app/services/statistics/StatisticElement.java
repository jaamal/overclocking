package services.statistics;

import models.CompressionAlgorithmType;

public class StatisticElement
{
	public final Class<?> statisticClass;
	public final CompressionAlgorithmType algorithmType;
	
	public StatisticElement(Class<?> statisticClass, CompressionAlgorithmType algorithmType) {
		this.statisticClass = statisticClass;
		this.algorithmType = algorithmType;
	}

	@Override
	public int hashCode()
	{
		return statisticClass.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (!(StatisticElement.class == obj.getClass()))
			return false;
		
		StatisticElement element = (StatisticElement) obj;
		return element.statisticClass.equals(this.statisticClass);
	}
}
