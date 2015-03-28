package patternMatching.fcpm.table.concurrent.hash;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.table.IAPTable;

import java.util.concurrent.ConcurrentHashMap;

public final class ConcurrentBuildInHashTableBasedTable implements IAPTable
{
    private final int patternSize;
    private final int textSize;

    private final class Cell
    {
        private final int patternIndex;
        private final int textIndex;
        private final int hashCode;

        public Cell(int patternIndex, int textIndex)
        {
            this.patternIndex = patternIndex;
            this.textIndex = textIndex;
            this.hashCode = 31 * patternIndex + textIndex;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cell cell = (Cell) o;

            if (patternIndex != cell.patternIndex) return false;
            if (textIndex != cell.textIndex) return false;

            return true;
        }

        @Override
        public int hashCode()
        {
            return hashCode;
        }
    }


    private final ConcurrentHashMap<Cell, ArithmeticProgression> internalMap;

    public ConcurrentBuildInHashTableBasedTable(int patternSize, int textSize)
    {
        this.patternSize = patternSize;
        this.textSize = textSize;
        this.internalMap = new ConcurrentHashMap<>(16, 0.75f, 4);
    }

    @Override
    public ArithmeticProgression get(int patternIndex, int textIndex)
    {
        ArithmeticProgression progression = internalMap.get(new Cell(patternIndex, textIndex));
        return progression == null ? ArithmeticProgression.Empty : progression;
    }

    @Override
    public void set(int patternIndex, int textIndex, ArithmeticProgression progression)
    {
        if (progression.isEmpty())
            return;
        internalMap.put(new Cell(patternIndex, textIndex), progression);
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
