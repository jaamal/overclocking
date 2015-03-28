package services.statistics.scaling;

import java.util.List;
import models.statistics.StatisticType;

public class CompositeStatisticScaler implements ICompositeStatisticScaler
{
	private final IStatsisticScaler[] scalers;

	public CompositeStatisticScaler(IStatsisticScaler... scalers){
		this.scalers = scalers;
	}
	
	@Override
	public ScalingResult scale(List<Object[]> values, StatisticType statisticType)
	{
		for (IStatsisticScaler scaler : scalers) {
			if (scaler.getSupportedType() == statisticType.unitType)
				return scaler.scale(values);
		}
		throw new ScalerNotFoundException(statisticType);
	}

}
