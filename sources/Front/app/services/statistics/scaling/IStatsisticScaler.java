package services.statistics.scaling;

import java.util.List;
import models.statistics.UnitType;

public interface IStatsisticScaler
{
	UnitType getSupportedType();
	ScalingResult scale(List<Object[]> values);
}
