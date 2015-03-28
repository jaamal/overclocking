package services.statistics.scaling;

import java.util.ArrayList;
import java.util.List;
import models.statistics.UnitType;

public class TimeScaler implements IStatsisticScaler
{
	private static final float ms = 1;
	private static final float s = 1000;
	private static final float min = s * 60;
	private static final float hour = min * 60;
	
	private static final int magicNumber = 10; 

	@Override
	public UnitType getSupportedType()
	{
		return UnitType.time;
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
	
	private static float calcReductionRate(long maxValue) {
		if (maxValue > magicNumber * hour)
			return hour;
		if (maxValue > magicNumber * min)
			return min;
		if (maxValue > magicNumber * s)
			return s;
		return ms;
	}
	
	private static String getUnitString(float reductionRate){
		if (hour == reductionRate)
			return "hour";
		if (min == reductionRate)
			return "min";
		if (s == reductionRate)
			return "s";
		return "ms";
	}
	
}
