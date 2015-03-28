package services.statistics.scaling;

import models.statistics.StatisticType;

public class ScalerNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = -137659507409675174L;

	public ScalerNotFoundException(StatisticType statisticType) {
		super(String.format("Scaler for type %s was not found.", statisticType.toString()));
	}
}
