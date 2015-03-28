package services.statistics.mapping;

import models.CompressionAlgorithmType;

public class AlgorithmTypeMappingNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 555227915263149230L;
	
	public AlgorithmTypeMappingNotFoundException(CompressionAlgorithmType algorithmType) {
		super(String.format("Mapping for algorithm type %s not found.", algorithmType.toString()));
	}
}
