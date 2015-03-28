package patternMatching.fcpm.table;

public final class DebuggingTableFactory implements IAPTableFactory
{
    private final IAPTableFactory tableFactory;
    private final IAPTableStatisticsFactory tableStatisticsFactory;

    public DebuggingTableFactory(IAPTableFactory tableFactory, IAPTableStatisticsFactory tableStatisticsFactory)
    {
        this.tableFactory = tableFactory;
        this.tableStatisticsFactory = tableStatisticsFactory;
    }

    @Override
    public IAPTable create(int patternSize, int textSize)
    {
        return new DebuggingAPTable(tableFactory.create(patternSize, textSize), tableStatisticsFactory);
    }
}
