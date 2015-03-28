package patternMatching.fcpm.table.builder;

public final class OrderedTextIterator implements ITextIterator {
    private int currentIndex;
    private final int initialIndex;
    private final int total;
    private final int batchCount;

    public OrderedTextIterator(int index, int total, int batchCount) {
        this.currentIndex = index;
        this.initialIndex = index;
        this.total = total;
        this.batchCount = batchCount;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < total;
    }

    @Override
    public int next() {
        int result = currentIndex;
        currentIndex += batchCount;
        return result;
    }

    @Override
    public void reset() {
        currentIndex = initialIndex;
    }
}
