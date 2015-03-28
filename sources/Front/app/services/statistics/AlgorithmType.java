package services.statistics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import models.CompressionAlgorithmType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AlgorithmType
{

	CompressionAlgorithmType algorithmType();
}
