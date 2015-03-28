package factories;

import models.MeasurableValueModel;

public class MeasurableValuesFactory implements IMeasurableValuesFactory
{
	private final static String MHzUnit = "MHz";
	private final static int MHz = 1000000;
	
	private final static String MbUnit = "Mb";
	private final static int Mb = 1024 * 1024;
	
	private final static String Percent = "%";
	
	@Override
	public MeasurableValueModel createMHz(long hzValue)
	{
		return new MeasurableValueModel(Integer.toString((int) (hzValue / MHz)), MHzUnit);
	}

	@Override
	public MeasurableValueModel createMb(long bytes)
	{
		return new MeasurableValueModel(Integer.toString((int) (bytes / Mb)), MbUnit);
	}

	@Override
	public MeasurableValueModel createPercent(float value)
	{
		return new MeasurableValueModel(String.format("%.2f", value), Percent);
	}

	@Override
	public MeasurableValueModel createNumeric(int value)
	{
		return new MeasurableValueModel(Integer.toString(value), "");
	}

	@Override
	public MeasurableValueModel createNumeric(long value)
	{
		return new MeasurableValueModel(Long.toString(new Long(value)), "");
	}
}
