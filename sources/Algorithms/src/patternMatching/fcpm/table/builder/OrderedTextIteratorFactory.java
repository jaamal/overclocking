package patternMatching.fcpm.table.builder;

public final class OrderedTextIteratorFactory implements ITextIteratorFactory {
    @Override
    public ITextIterator create(int index, int total, int batchSize, int batchesCount) {
        return new OrderedTextIterator(index, total, batchesCount);
    }
}
