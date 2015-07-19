package sorting;

public class ComplexFunnelMergerParameters
{
    public ComplexFunnelMergerParameters(long innerMergersCount, long streamCountForMerger, long resultSize)
    {
        this.innerMergersCount = innerMergersCount;
        this.streamCountForMerger = streamCountForMerger;
        this.resultSize = resultSize;
    }

    public final long innerMergersCount;
    public final long streamCountForMerger;
    public final long resultSize;
}
