package services.statistics.scaling;

import java.util.ArrayList;
import java.util.List;
import models.statistics.UnitType;

public class PercentScaler implements IStatsisticScaler
{
	@Override
	public UnitType getSupportedType()
	{
		return UnitType.percent;
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
				scaledPoints[i] = Math.round((Float) objects[i]);
			}
			result.add(scaledPoints);
		}
		return new ScalingResult(result, "%");
	}
}
