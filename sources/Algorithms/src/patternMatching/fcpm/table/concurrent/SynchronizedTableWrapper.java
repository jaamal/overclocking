package patternMatching.fcpm.table.concurrent;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.table.IAPTable;

public class SynchronizedTableWrapper implements IAPTable
{
    private final IAPTable table;

    public SynchronizedTableWrapper(IAPTable table)
    {
        this.table = table;
    }

    @Override
    public synchronized ArithmeticProgression get(int patternIndex, int textIndex)
    {
        return table.get(patternIndex, textIndex);
    }

    @Override
    public synchronized void set(int patternIndex, int textIndex, ArithmeticProgression progression)
    {
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
}
