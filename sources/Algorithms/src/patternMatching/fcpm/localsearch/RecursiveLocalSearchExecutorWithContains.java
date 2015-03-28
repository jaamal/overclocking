package patternMatching.fcpm.localsearch;

import patternMatching.fcpm.IPatternMatchingContext;
import patternMatching.fcpm.preprocessing.Product;

public final class RecursiveLocalSearchExecutorWithContains implements ILocalSearchExecutor
{
    private final ILocalSearchResultFactory localSearchResultFactory;

    public RecursiveLocalSearchExecutorWithContains(ILocalSearchResultFactory localSearchResultFactory)
    {
        this.localSearchResultFactory = localSearchResultFactory;
    }

    @Override
    public ILocalSearchResult find(IPatternMatchingContext context, int patternIndex, int textIndex, int alpha, int beta)
    {
        int textLength = context.getText(textIndex).Length;
        if (alpha >= textLength)
            return LocalSearchResult.Empty;
        if (beta < 0)
            return LocalSearchResult.Empty;
        if (alpha < 0)
            alpha = 0;
        if (beta >= textLength)
            beta = textLength - 1;
        IEditableLocalSearchResult result = localSearchResultFactory.create();
        Product pattern = context.getPattern(patternIndex);
        IndexedProduct indexedPattern = new IndexedProduct (patternIndex, pattern);
        findInternal(context, indexedPattern, textIndex, alpha, beta, 0, result);
        return result;
    }

    @Override
    public boolean contains(IPatternMatchingContext context, int patternIndex, int textIndex, int alpha, int beta)
    {
        int textLength = context.getText(textIndex).Length;
        if (alpha >= textLength)
            return false;
        if (beta < 0)
            return false;
        if (alpha < 0)
            alpha = 0;
        if (beta >= textLength)
            beta = textLength - 1;
        IEditableLocalSearchResult result = localSearchResultFactory.create();
        Product pattern = context.getPattern(patternIndex);
        IndexedProduct indexedPattern = new IndexedProduct (patternIndex, pattern);
        containsInternal(context, indexedPattern, textIndex, alpha, beta, 0, result);
        return !result.isEmpty();
    }

    private void findInternal(IPatternMatchingContext context, IndexedProduct pattern, int textIndex, int alpha, int beta, int globalShift, IEditableLocalSearchResult result)
    {
        if (beta - alpha < pattern.Length - 1)
            return;
        Product text = context.getText(textIndex);
        if (!text.IsTerminal)
        {
            if (text.CutPosition - alpha >= pattern.Length)
                findInternal(context, pattern, text.FirstProduct, alpha, Math.min(beta, text.CutPosition - 1), globalShift, result);
        }
        int truncationBegin = alpha;
        int truncationEnd = beta - pattern.Length + 1;
        if (truncationBegin <= truncationEnd)
            result.add(context.getAPTable().get(pattern.Index, textIndex).truncate(truncationBegin, truncationEnd).shift(globalShift));
        if (!text.IsTerminal)
        {
            if (beta - text.CutPosition + 1 >= pattern.Length)
                findInternal(context, pattern, text.SecondProduct, Math.max(0, alpha - text.CutPosition), beta - text.CutPosition, globalShift + text.CutPosition, result);
        }
    }

    private void containsInternal(IPatternMatchingContext context, IndexedProduct pattern, int textIndex, int alpha, int beta, int globalShift, IEditableLocalSearchResult result) {
        if(! result.isEmpty())
            return;
        int patternLength = pattern.Length;
        if (beta - alpha < pattern.Length - 1)
            return;
        Product text = context.getText(textIndex);
        if (!text.IsTerminal)
        {
            if (text.CutPosition - alpha >= patternLength)
                containsInternal(context, pattern, text.FirstProduct, alpha, Math.min(beta, text.CutPosition - 1), globalShift, result);
        }
        int truncationBegin = alpha;
        int truncationEnd = beta - patternLength + 1;
        if (truncationBegin <= truncationEnd)
            result.add(context.getAPTable().get(pattern.Index, textIndex).truncate(truncationBegin, truncationEnd).shift(globalShift));
        if(! result.isEmpty())
            return;
        if (!text.IsTerminal)
        {
            if (beta - text.CutPosition + 1 >= patternLength)
                containsInternal(context, pattern, text.SecondProduct, Math.max(0, alpha - text.CutPosition), beta - text.CutPosition, globalShift + text.CutPosition, result);
        }
    }

}
 