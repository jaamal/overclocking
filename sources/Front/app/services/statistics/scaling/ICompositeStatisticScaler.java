package services.statistics.scaling;

import java.util.List;
import models.statistics.StatisticType;

public interface ICompositeStatisticScaler
{
	ScalingResult scale(List<Object[]> values, StatisticType statisticType);
}
