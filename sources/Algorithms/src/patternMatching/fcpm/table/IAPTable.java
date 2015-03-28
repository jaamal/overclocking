package patternMatching.fcpm.table;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public interface IAPTable {
    ArithmeticProgression get(int patternIndex, int textIndex);

    void set(int patternIndex, int textIndex, ArithmeticProgression ap);

    int patternSize();

    int textSize();
}
