package patternMatching.fcpm.table.builder;

public interface ITextIteratorFactory {
    ITextIterator create(int index, int total, int batchSize, int batchesCount);
}
