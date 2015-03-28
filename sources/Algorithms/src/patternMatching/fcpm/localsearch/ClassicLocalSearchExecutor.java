package patternMatching.fcpm.localsearch;

import patternMatching.fcpm.IPatternMatchingContext;
import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.preprocessing.Product;

public class ClassicLocalSearchExecutor implements ILocalSearchExecutor {
    private ILocalSearchResultFactory localSearchResultFactory;

    public ClassicLocalSearchExecutor(ILocalSearchResultFactory localSearchResultFactory) {
        this.localSearchResultFactory = localSearchResultFactory;
    }

    private static final class Node {
        public Node Next;
        public final ArithmeticProgression Progression;

        public Node(ArithmeticProgression progression) {
            Progression = progression;
        }


        public Node(ArithmeticProgression progression, Node next) {
            Progression = progression;
            Next = next;
        }
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
        IndexedProduct indexedPattern = new IndexedProduct(patternIndex, pattern);
        Node head = new Node(ArithmeticProgression.Empty);
        findInternal(context, indexedPattern, textIndex, alpha, beta, 0, head);
        return merge(head);
    }

    private ILocalSearchResult merge(Node head) {
        IEditableLocalSearchResult localSearchResult = localSearchResultFactory.create();
        Node node = head;
        while (node != null) {
            localSearchResult.add(node.Progression);
            node = node.Next;
        }
        return localSearchResult;
    }

    private void findInternal(IPatternMatchingContext context, IndexedProduct pattern, int textIndex, int alpha, int beta, int globalShift, Node node) {
        int truncationBegin = alpha;
        int truncationEnd = beta - pattern.Length + 1;
        Node next;
        if (truncationBegin <= truncationEnd) {
            ArithmeticProgression ap = context.getAPTable().get(pattern.Index, textIndex).truncate(truncationBegin, truncationEnd).shift(globalShift);
            next = new Node(ap, node.Next);
        } else {
            next = new Node(ArithmeticProgression.Empty, node.Next);
        }
        node.Next = next;
        if (beta - alpha < pattern.Length - 1)
            return;
        Product text = context.getText(textIndex);
        if(text.IsTerminal)
            return;
        if (text.CutPosition - alpha >= pattern.Length)
            findInternal(context, pattern, text.FirstProduct, alpha, Math.min(beta, text.CutPosition - 1), globalShift, node);

        if (beta - text.CutPosition + 1 >= pattern.Length)
            findInternal(context, pattern, text.SecondProduct, Math.max(0, alpha - text.CutPosition), beta - text.CutPosition, globalShift + text.CutPosition, next);

    }

    @Override
    public boolean contains(IPatternMatchingContext context, int patternIndex, int textIndex, int alpha, int beta) {
        return !find(context, patternIndex, textIndex, alpha, beta).isEmpty();
    }
}
