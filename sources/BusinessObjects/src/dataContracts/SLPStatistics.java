package dataContracts;

public class SLPStatistics
{
    public final long length;
    public final long countRules;
    public final long height;

    public SLPStatistics(
            long length,
            long countRules,
            long height)
    {
        this.length = length;
        this.countRules = countRules;
        this.height = height;
    }
}
