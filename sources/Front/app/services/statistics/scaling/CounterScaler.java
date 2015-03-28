package services.statistics.scaling;

import java.util.ArrayList;
import java.util.List;
import models.statistics.UnitType;

public class CounterScaler implements IStatsisticScaler
{
	
	@Override
	public UnitType getSupportedType()
	{
		return UnitType.counter;
	}

	@Override
	public ScalingResult scale(List<Object[]> values)
	{
		if (values == null || values.size() == 0)
			return ScalingResult.Empty();
		
		List<int[]> result = new ArrayList<int[]>();
		for (Object[] objects : values) {
			int[] scaledPoints = new int[objects.length];
			for (int i = 0; i < objects.length; i++) {
				scaledPoints[i] = objects[i] instanceof Integer 
										? (Integer) objects[i] 
										: ((Long) objects[i]).intValue();
			}
			result.add(scaledPoints);
		}
		return new ScalingResult(result, "number");
	}

}
