package patternMatching.fcpm.table;

public class APTableStatisticsFactory implements IAPTableStatisticsFactory
{
    @Override
    public IAPTableStatistics create()
    {
        return new APTableStatistics();
    }
}
