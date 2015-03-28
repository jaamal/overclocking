package patternMatching.fcpm.table;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public interface IAPTableStatistics
{
    void set(int patternIndex, int textIndex, ArithmeticProgression progression);

    int emptyCount();

    int totalCount();
}
