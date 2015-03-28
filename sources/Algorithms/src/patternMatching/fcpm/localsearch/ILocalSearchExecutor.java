package patternMatching.fcpm.localsearch;

import patternMatching.fcpm.IPatternMatchingContext;

public interface ILocalSearchExecutor
{
    ILocalSearchResult find(IPatternMatchingContext context, int patternIndex, int textIndex, int alpha, int beta);
    boolean contains(IPatternMatchingContext context, int patternIndex, int textIndex, int alpha, int beta);
}


