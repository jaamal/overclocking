package models.statistics;

public abstract class CompressionStatsBase
{
	protected static float calcCompressionRate(long sourceTextSize, long compressedTextSize) throws InvalidParameterException {
		if (sourceTextSize == 0)
			throw new InvalidParameterException("sourceFileSize", String.valueOf(sourceTextSize));
		return ((float) compressedTextSize / sourceTextSize) * 100;
	}
}
