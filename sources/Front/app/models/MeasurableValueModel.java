package models;

public class MeasurableValueModel
{
	public final String value;
	public final String unit;
	
	public MeasurableValueModel(String value, String unit) {
		this.value = value;
		this.unit = unit;
	}

	@Override
	public String toString()
	{
		return value + " " + unit;
	}
}
