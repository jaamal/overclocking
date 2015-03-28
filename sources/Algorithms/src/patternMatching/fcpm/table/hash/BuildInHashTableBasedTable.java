package patternMatching.fcpm.table.hash;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.table.IAPTable;

import java.util.HashMap;

public class BuildInHashTableBasedTable implements IAPTable {

    private static class CellIndex {
        public final int patternIndex;
        public final int textIndex;
        private final int hashCode;

        public CellIndex(int patternIndex, int textIndex) {
            this.patternIndex = patternIndex;
            this.textIndex = textIndex;
            this.hashCode = generateHashCode(patternIndex, textIndex);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CellIndex cellIndex = (CellIndex) o;

            if (patternIndex != cellIndex.patternIndex) return false;
            if (textIndex != cellIndex.textIndex) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        private static int generateHashCode(int patternIndex, int textIndex) {
            int result = patternIndex;
            result = 31 * result + textIndex;
            return result;
        }
    }
    private final HashMap<CellIndex, ArithmeticProgression> internal;
    private final int patternSize;
    private final int textSize;

    public BuildInHashTableBasedTable(int patternSize, int textSize) {
        this.patternSize = patternSize;
        this.textSize = textSize;
        this.internal = new HashMap<>();
    }

    @Override
    public ArithmeticProgression get(int patternIndex, int textIndex) {
        CellIndex key = new CellIndex(patternIndex, textIndex);
        ArithmeticProgression progression = internal.get(key);
        if(progression == null)
            return ArithmeticProgression.Empty;
        return progression;
    }

    @Override
    public void set(int patternIndex, int textIndex, ArithmeticProgression progression) {
        if(progression.isEmpty())
            return;
        CellIndex key = new CellIndex(patternIndex, textIndex);
        internal.put(key, progression);
    }

    @Override
    public int patternSize() {
        return patternSize;
    }

    @Override
    public int textSize() {
        return textSize;
    }
}
