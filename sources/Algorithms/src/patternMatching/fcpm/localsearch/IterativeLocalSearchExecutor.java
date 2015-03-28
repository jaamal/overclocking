package patternMatching.fcpm.localsearch;

import java.util.ArrayDeque;
import java.util.Deque;

import patternMatching.fcpm.IPatternMatchingContext;
import patternMatching.fcpm.preprocessing.Product;

public class IterativeLocalSearchExecutor implements ILocalSearchExecutor {

    private ILocalSearchResultFactory localSearchResultFactory;

    public IterativeLocalSearchExecutor(ILocalSearchResultFactory localSearchResultFactory) {
        this.localSearchResultFactory = localSearchResultFactory;
    }

    @Override
    public ILocalSearchResult find(IPatternMatchingContext context, int patternIndex, int textIndex, int alpha, int beta) {
        int textLength = context.getText(textIndex).Length;
        if (alpha >= textLength)
            return LocalSearchResult.Empty;
        if (beta < 0)
            return LocalSearchResult.Empty;
        if (alpha < 0)
            alpha = 0;
        if (beta >= textLength)
            beta = textLength - 1;

        Product pattern = context.getPattern(patternIndex);
        int patternLength = pattern.Length;

        IEditableLocalSearchResult localSearchResult = localSearchResultFactory.create();
        Deque<Arguments> arguments = new ArrayDeque<>(32);
        arguments.push(new Arguments(textIndex, alpha, beta, 0));
        while (!arguments.isEmpty()) {
            Arguments arg = arguments.peek();
            Product text = context.getText(arg.TextIndex);
            int currentAlpha = arg.Alpha;
            int currentBeta = arg.Beta;
            if(currentBeta - currentAlpha < patternLength - 1) {
                arguments.pop();
                continue;
            }

            if (!arg.IsFirstVisited && !text.IsTerminal) {
                arg.IsFirstVisited = true;
                if (text.CutPosition - currentAlpha >= patternLength) {
                    arguments.push(new Arguments(text.FirstProduct, currentAlpha, Math.min(currentBeta, text.CutPosition - 1), arg.GlobalShift));
                    continue;
                }
            }

            int truncationBegin = currentAlpha;
            int truncationEnd = currentBeta - patternLength + 1;
            if (truncationBegin <= truncationEnd)
                localSearchResult.add(context.getAPTable().get(patternIndex, arg.TextIndex).truncate(truncationBegin, truncationEnd).shift(arg.GlobalShift));
            arguments.pop();

            if (!text.IsTerminal) {
                if (currentBeta - text.CutPosition + 1 >= patternLength)
                    arguments.push(new Arguments(text.SecondProduct, Math.max(0, currentAlpha - text.CutPosition), currentBeta - text.CutPosition, arg.GlobalShift + text.CutPosition));
            }
        }

        return localSearchResult;
    }

    @Override
    public boolean contains(IPatternMatchingContext context, int patternIndex, int textIndex, int alpha, int beta) {
        return !find(context, patternIndex, textIndex, alpha, beta).isEmpty();
    }

    public static final class Arguments {
        public final int TextIndex;
        public final int Alpha;
        public final int Beta;
        public final int GlobalShift;
        public boolean IsFirstVisited;

        public Arguments(int textIndex, int alpha, int beta, int globalShift) {
            this.TextIndex = textIndex;
            this.Alpha = alpha;
            this.Beta = beta;
            this.GlobalShift = globalShift;
            this.IsFirstVisited = false;
        }
    }
}
