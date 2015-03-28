package patternMatching.fcpm.table.builder;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public interface IArithmeticProgressionList {
    void add(ArithmeticProgression ap);
    ArithmeticProgression get(int index);
    int size();
    void reset();
}
