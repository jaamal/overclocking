package sorting;

public interface IComplexFunnelMergerParametersGetter
{
    ComplexFunnelMergerParameters getParameters(long inputStreamCount);
    long getResultSize(long inputStreamCount);
}
