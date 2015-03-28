package factories;

import models.MeasurableValueModel;

public interface IMeasurableValuesFactory
{
	MeasurableValueModel createMHz(long hzValue);
	MeasurableValueModel createMb(long bytes);
	MeasurableValueModel createPercent(float value);
	MeasurableValueModel createNumeric(int value);
	MeasurableValueModel createNumeric(long value);
}
