package patternMatching.fcpm.table.array;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.table.IAPTable;

public final class ArrayBasedTable implements IAPTable
{
    private final ArithmeticProgression[][] internalTable;
    private final int patternSize;
    private final int textSize;

    public ArrayBasedTable(int patternSize, int textSize)
    {
        this.patternSize = patternSize;
        this.textSize = textSize;
        internalTable = new ArithmeticProgression[patternSize][textSize];
        for (int i = 0; i < patternSize; ++i)
            internalTable[i] = new ArithmeticProgression[textSize];
    }

    @Override
    public ArithmeticProgression get(int patternIndex, int textIndex)
    {
        return internalTable[patternIndex][textIndex];
    }

    @Override
    public void set(int patternIndex, int textIndex, ArithmeticProgression progression)
    {
        internalTable[patternIndex][textIndex] = progression;
    }

    @Override
    public int patternSize()
    {
        return patternSize;
    }

    @Override
    public int textSize()
    {
        return textSize;
    }
}
