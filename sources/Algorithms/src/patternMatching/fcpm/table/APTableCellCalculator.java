package patternMatching.fcpm.table;

import static patternMatching.fcpm.arithmeticProgression.ArithmeticProgression.Empty;
import patternMatching.fcpm.IPatternMatchingContext;
import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.localsearch.ILocalSearchExecutor;
import patternMatching.fcpm.localsearch.ILocalSearchResult;
import patternMatching.fcpm.preprocessing.Product;

public final class APTableCellCalculator implements IAPTableCellCalculator
{
    private final ILocalSearchExecutor localSearchExecutor;

    public APTableCellCalculator(ILocalSearchExecutor localSearchExecutor)
    {
        this.localSearchExecutor = localSearchExecutor;
    }

    @Override
    public ArithmeticProgression calculate(IPatternMatchingContext context, int patternIndex, int textIndex)
    {
        Product pattern = context.getPattern(patternIndex);
        Product text = context.getText(textIndex);

        ArithmeticProgression occurrences = Empty;
        if (pattern.IsTerminal)
        {
            if (text.IsTerminal)
            {
                if (pattern.FirstLetter == text.LastLetter)
                    occurrences = ArithmeticProgression.create(0);
            } else
            {
                Product first = context.getText(text.FirstProduct);
                Product second = context.getText(text.SecondProduct);
                if (pattern.LastLetter == first.LastLetter)
                    occurrences = occurrences.merge(ArithmeticProgression.create(text.CutPosition - 1));
                if (pattern.FirstLetter == second.FirstLetter)
                    occurrences = occurrences.merge(ArithmeticProgression.create(text.CutPosition));
            }
        } else
        {
            if (pattern.Length <= text.Length)
            {
                int firstPatternIndex = pattern.FirstProduct;
                int secondPatternIndex = pattern.SecondProduct;
                Product first = context.getPattern(firstPatternIndex);
                Product second = context.getPattern(secondPatternIndex);
                if (first.Length >= second.Length)
                    occurrences = processFirstBiggerSecond(context, pattern, textIndex, text, firstPatternIndex, secondPatternIndex, first, second);
                else
                    occurrences = processFirstSmallerSecond(context, pattern, textIndex, text, firstPatternIndex, secondPatternIndex, first, second);
            }
        }


        return occurrences;
    }


    private ArithmeticProgression processFirstBiggerSecond(IPatternMatchingContext context, Product pattern, int textIndex, Product text, int biggerPartIndex, int smallerPartIndex, Product biggerPart, Product smallerPart)
    {
        int alpha = Math.max(0, text.CutPosition - pattern.Length);
        int beta = text.CutPosition + biggerPart.Length - 1;
        ILocalSearchResult biggerPartOccurrences = localSearchExecutor.find(context, biggerPartIndex, textIndex, alpha, beta);
        ArithmeticProgression result = Empty;
        for (int i = 0; i < biggerPartOccurrences.size(); ++i)
        {
            ArithmeticProgression smallerPartOccurrenceBeginning = biggerPartOccurrences.get(i).shift(biggerPart.Length);
            if (smallerPartOccurrenceBeginning.isEmpty())
                continue;
            int continentalBegin = alpha;
            //At least second.Length far from last ending
            int continentalEnd = smallerPartOccurrenceBeginning.lastElement() - smallerPart.Length;
            ArithmeticProgression continentalBeginning = continentalBegin < continentalEnd ? smallerPartOccurrenceBeginning.truncate(continentalBegin, continentalEnd) : Empty;
            if (!continentalBeginning.isEmpty())
            {
                int continentalAlpha = continentalBeginning.firstElement;
                int continentalBeta = continentalBeginning.firstElement + smallerPart.Length - 1;
                if (localSearchExecutor.contains(context, smallerPartIndex, textIndex, continentalAlpha, continentalBeta))
                    result = result.merge(continentalBeginning);
            }
            //We should add one to ending
            int seasideBegin = smallerPartOccurrenceBeginning.lastElement() - smallerPart.Length + 1;
            int seasideEnd = smallerPartOccurrenceBeginning.lastElement() + smallerPart.Length;
            ArithmeticProgression seasideBeginning = smallerPartOccurrenceBeginning.truncate(seasideBegin, seasideEnd);
            if (!seasideBeginning.isEmpty())
            {
                int seasideAlpha = seasideBegin;
                int seasideBeta = seasideEnd;
                ILocalSearchResult smallPartOccurrences = localSearchExecutor.find(context, smallerPartIndex, textIndex, seasideAlpha, seasideBeta);
                for (int j = 0; j < smallPartOccurrences.size(); ++j)
                    result = result.merge(seasideBeginning.intersect(smallPartOccurrences.get(j)));
            }
        }
        return result.shift(-(biggerPart.Length));
    }

    private ArithmeticProgression processFirstSmallerSecond(IPatternMatchingContext context, Product pattern, int textIndex, Product text, int firstPatternIndex, int secondPatternIndex, Product first, Product second)
    {
        int alpha = text.CutPosition - second.Length;
        int beta = text.CutPosition + pattern.Length - 1;
        ILocalSearchResult secondOccurrences = localSearchExecutor.find(context, secondPatternIndex, textIndex, alpha, beta);
        ArithmeticProgression result = Empty;
        for (int i = 0; i < secondOccurrences.size(); ++i)
        {
            ArithmeticProgression secondOccurrencesPart = secondOccurrences.get(i);
            ArithmeticProgression seasidePart = secondOccurrencesPart.truncate(alpha, secondOccurrencesPart.firstElement + first.Length - 1);
            ArithmeticProgression continentalPart = Empty;
            if (beta >= secondOccurrencesPart.firstElement + first.Length)
                continentalPart = secondOccurrencesPart.truncate(secondOccurrencesPart.firstElement + first.Length, beta);

            if (!seasidePart.isEmpty())
            {
                int alpha1 = seasidePart.firstElement - first.Length;
                int beta1 = seasidePart.lastElement() - 1;
                ILocalSearchResult searchResult = localSearchExecutor.find(context, firstPatternIndex, textIndex, alpha1, beta1);
                for (int j = 0; j < searchResult.size(); ++j)
                    result = result.merge(seasidePart.shift(-first.Length).intersect(searchResult.get(j)));
            }
            if (!continentalPart.isEmpty())
            {
                int alpha1 = continentalPart.firstElement - first.Length;
                int beta1 = continentalPart.firstElement - 1;
                if (localSearchExecutor.contains(context, firstPatternIndex, textIndex, alpha1, beta1))
                    result = result.merge(continentalPart.shift(-first.Length));
            }
        }
        return result;
    }
}
