package sorting;

import java.util.concurrent.ConcurrentHashMap;

public class ComplexFunnelMergerParametersGetter implements IComplexFunnelMergerParametersGetter
{
    public static final ComplexFunnelMergerParametersGetter instance = new ComplexFunnelMergerParametersGetter();

    private ConcurrentHashMap<Long, ComplexFunnelMergerParameters> calculated;

    private ComplexFunnelMergerParametersGetter()
    {
        calculated = new ConcurrentHashMap<Long, ComplexFunnelMergerParameters>();
    }

    @Override
    public ComplexFunnelMergerParameters getParameters(long inputStreamCount)
    {
        if (inputStreamCount <= 2)
            throw new RuntimeException("inputStreamCount must be greater than 2");
        if (calculated.containsKey(inputStreamCount))
            return calculated.get(inputStreamCount);
        long innerMergersCount = (long)Math.ceil(Math.pow(inputStreamCount, 0.5));
        long streamCountForMerger = (inputStreamCount + innerMergersCount - 1) / innerMergersCount;
        long divisor = getResultSize(innerMergersCount);
        long resultSize = (long)Math.pow(inputStreamCount, 3.0);
        resultSize += (divisor - resultSize % divisor) % divisor;
        ComplexFunnelMergerParameters result = new ComplexFunnelMergerParameters(innerMergersCount, streamCountForMerger, resultSize);
        calculated.put(inputStreamCount, result);
        return result;
    }

    @Override
    public long getResultSize(long inputStreamCount)
    {
        if (inputStreamCount <= 2)
            return 8;
        return getParameters(inputStreamCount).resultSize;
    }
}
