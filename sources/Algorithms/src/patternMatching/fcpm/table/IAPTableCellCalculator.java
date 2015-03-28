package patternMatching.fcpm.table;

import patternMatching.fcpm.IPatternMatchingContext;
import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public interface IAPTableCellCalculator
{
    ArithmeticProgression calculate(IPatternMatchingContext context, int patternIndex, int textIndex);
}
