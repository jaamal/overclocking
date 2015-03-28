package services.statistics;

import models.CompressionAlgorithmType;

public class StatisticInfo
{
	public final CompressionAlgorithmType algorithmType;
	public final Object[] xValues;
	public final Object[] yValues;
	
	public StatisticInfo(CompressionAlgorithmType algorithmType, Object[] xValues, Object[] yValues) {
		this.algorithmType = algorithmType;
		this.xValues = xValues;
		this.yValues = yValues;
	}
}
