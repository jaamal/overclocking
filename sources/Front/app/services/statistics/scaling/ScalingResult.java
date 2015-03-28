package services.statistics.scaling;

import java.util.List;

public class ScalingResult
{
	private static final ScalingResult empty = new ScalingResult(null, null);
	
	public final List<int[]> values;
	//TODO: try to change this field on enum
	public final String unit;
	
	public ScalingResult(List<int[]> values, String unit) {
		this.values = values;
		this.unit = unit;
	}
	
	public boolean isEmpty() {
		return this == empty;
	}
	
	public static ScalingResult Empty() {
		return empty;
	}
}
