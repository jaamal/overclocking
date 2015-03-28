package patternMatching.fcpm.table;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public class APTableStatistics implements IAPTableStatistics
{
    private int emptyCount = 0;
    private int totalCount = 0;

    @Override
    public void set(int patternIndex, int textIndex, ArithmeticProgression progression)
    {
        if (progression.isEmpty())
            ++emptyCount;
        ++totalCount;
    }

    @Override
    public int emptyCount()
    {
        return emptyCount;
    }

    @Override
    public int totalCount()
    {
        return totalCount;
    }
}
