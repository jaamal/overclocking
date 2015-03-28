package patternMatching.fcpm.localsearch;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public interface ILocalSearchResult {
    ArithmeticProgression get(int index);
    boolean isEmpty();
    int size();
}

