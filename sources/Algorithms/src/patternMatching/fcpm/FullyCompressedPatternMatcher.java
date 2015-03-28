package patternMatching.fcpm;

import patternMatching.IPatternMatcher;
import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.IAPTable;

public class FullyCompressedPatternMatcher implements IPatternMatcher
{
    private final Product[] pattern;
    private final Product[] text;
    private final IAPTable table;

    public FullyCompressedPatternMatcher(Product[] pattern, Product[] text, IAPTable table)
    {
        this.pattern = pattern;
        this.text = text;
        this.table = table;
    }

    @Override
    public boolean contains()
    {
        int patternSize = pattern.length;
        int textSize = text.length;
        if (patternSize == 0 || textSize == 0)
            return false;
        for (int i = 0; i < textSize; ++i)
            if (!table.get(patternSize - 1, i).isEmpty())
                return true;
        return false;
    }

    @Override
    public int count()
    {
        int patternIndex = pattern.length - 1;
        int textIndex = text.length - 1;
        return countInternal(patternIndex, textIndex);
    }

    //TODO refactor this
    private int countInternal(int patternIndex, int textIndex)
    {
        ArithmeticProgression centralArithmeticProgression = table.get(patternIndex, textIndex);
        Product textProduct = this.text[textIndex];
        Product patternProduct = this.pattern[patternIndex];
        int occurrencesCount = centralArithmeticProgression.length();
        if (!textProduct.IsTerminal)
        {
            int cutPosition = textProduct.CutPosition;
            if (centralArithmeticProgression.contains(cutPosition))
                occurrencesCount--;
            if (centralArithmeticProgression.contains(cutPosition - patternProduct.Length))
                occurrencesCount--;
            occurrencesCount += countInternal(patternIndex, textProduct.FirstProduct) + countInternal(patternIndex, textProduct.SecondProduct);
        }
        return occurrencesCount;
    }

    @Override
    public boolean contains(int position)
    {
        int patternIndex = pattern.length - 1;
        int textIndex = text.length - 1;
        return containsInternal(patternIndex, textIndex, position);
    }

    //TODO refactor this
    private boolean containsInternal(int patternIndex, int textIndex, int position)
    {
        Product text = this.text[textIndex];
        Product pattern = this.pattern[patternIndex];
        if (!text.IsTerminal)
        {
            int cutPosition = text.CutPosition;
            if (position > cutPosition)
                return containsInternal(patternIndex, text.SecondProduct, position - this.text[text.FirstProduct].Length);
            if (position + pattern.Length < cutPosition)
                return containsInternal(patternIndex, text.FirstProduct, position);
        }
        return table.get(patternIndex, textIndex).contains(position);
    }
}

