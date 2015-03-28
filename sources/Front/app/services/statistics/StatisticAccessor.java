package services.statistics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import models.statistics.StatisticType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface StatisticAccessor
{
	StatisticType statisticType();
}
