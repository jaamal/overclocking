package patternMatching.fcpm.table.builder;

public final class IntervalTextIterator implements ITextIterator {
    private int currentIndex;
    private final int initialIndex;
    private int total;
    private int batchSize;

    public IntervalTextIterator(int index, int total, int batchSize) {
        if(index < 0)
            throw new IllegalArgumentException("index");
        if(total < 0)
            throw new IllegalArgumentException("total");
        if(batchSize <= 0)
            throw new IllegalArgumentException("batchSize");
        this.initialIndex = index * batchSize;
        this.total = total;
        this.batchSize = batchSize;
        this.currentIndex = index * batchSize;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < total && (currentIndex - initialIndex) < batchSize;
    }

    @Override
    public int next() {
        return currentIndex++;
    }

    @Override
    public void reset() {
        currentIndex = initialIndex;
    }
}
