package patternMatching.fcpm.table.builder;

public final class IntervalTextIteratorFactory implements ITextIteratorFactory {

    @Override
    public ITextIterator create(int index, int total, int batchSize, int batchesCount) {
        return new IntervalTextIterator(index, total, batchSize);
    }
}
