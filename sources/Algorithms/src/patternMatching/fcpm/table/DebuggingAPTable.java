package patternMatching.fcpm.table;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public final class DebuggingAPTable implements IAPTable
{
    private final IAPTableStatistics statistics;
    private final IAPTable table;

    public DebuggingAPTable(IAPTable table, IAPTableStatisticsFactory statisticsFactory)
    {
        this.table = table;
        this.statistics = statisticsFactory.create();
    }

    @Override
    public ArithmeticProgression get(int patternIndex, int textIndex)
    {
        return table.get(patternIndex, textIndex);
    }

    @Override
    public void set(int patternIndex, int textIndex, ArithmeticProgression progression)
    {
        statistics.set(patternIndex, textIndex, progression);
        table.set(patternIndex, textIndex, progression);
    }

    @Override
    public int patternSize()
    {
        return table.patternSize();
    }

    @Override
    public int textSize()
    {
        return table.textSize();
    }

    public IAPTableStatistics statistics()
    {
        return statistics;
    }
}
