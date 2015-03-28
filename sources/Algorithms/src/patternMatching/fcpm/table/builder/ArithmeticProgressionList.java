package patternMatching.fcpm.table.builder;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public final class ArithmeticProgressionList implements IArithmeticProgressionList {
    private int size;
    private final ArithmeticProgression[] internalList;

    public ArithmeticProgressionList(int capacity) {
        size = 0;
        internalList = new ArithmeticProgression[capacity];
    }

    public void add(ArithmeticProgression ap) {
        internalList[size++] = ap;
    }

    public ArithmeticProgression get(int index) {
        return internalList[index];
    }

    public void reset() {
        size = 0;
    }

    public int size() {
        return size;
    }
}
