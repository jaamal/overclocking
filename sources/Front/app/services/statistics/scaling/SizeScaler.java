package services.statistics.scaling;

import java.util.ArrayList;
import java.util.List;
import models.statistics.UnitType;

public class SizeScaler implements IStatsisticScaler
{
	private final static  float Kb = 1024;
	private final static float Mb = Kb * 1024;

	@Override
	public UnitType getSupportedType()
	{
		return UnitType.size;
	}

	@Override
	public ScalingResult scale(List<Object[]> values)
	{
		if (values == null || values.size() == 0)
			return ScalingResult.Empty();
		
		long maxValue = 0;
		for (Object[] objects : values) {
			for (int i = 0; i < objects.length; i++) {
				if ((Long)objects[i] > maxValue)
					maxValue = (Long)objects[i];
			}
		}

		float reductionRate = calcReductionRate(maxValue);	
		List<int[]> result = new ArrayList<int[]>();
		for (Object[] objects : values) {
			int[] scaledPoints = new int[objects.length];
			for (int i = 0; i < objects.length; i++) {
				scaledPoints[i] = Math.round(((Long)objects[i] / reductionRate));
			}
			result.add(scaledPoints);
		}
		return new ScalingResult(result, getUnitString(reductionRate));
	}
	
	private static float calcReductionRate(float maxValue) {
		if (maxValue > 10 * Mb)
			return Mb;
		return Kb;
	}
	
	private static String getUnitString(float reductionRate) {
		if (reductionRate == Mb)
			return "Mb";
		return "Kb";
	}

}
