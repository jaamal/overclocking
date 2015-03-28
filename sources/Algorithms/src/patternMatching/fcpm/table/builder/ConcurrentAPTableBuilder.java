package patternMatching.fcpm.table.builder;

import patternMatching.fcpm.IPatternMatchingContext;
import patternMatching.fcpm.IPatternMatchingContextFactory;
import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.IAPTableCellCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class ConcurrentAPTableBuilder implements IAPTableBuilder {

    private final int batchesCount;
    private final ITextIteratorFactory textIteratorFactory;
    private final IPatternMatchingContextFactory contextFactory;
    private final ExecutorService executor;
    private final IAPTableCellCalculator cellCalculator;
    private final IArithmeticProgressionListFactory listFactory;
    private final IConcurrentBuildingStatisticsFactory buildingStatisticsFactory;

    public ConcurrentAPTableBuilder(int batchesCount, ITextIteratorFactory textIteratorFactory, IPatternMatchingContextFactory contextFactory, IAPTableCellCalculator cellCalculator, IArithmeticProgressionListFactory listFactory, IConcurrentBuildingStatisticsFactory buildingStatisticsFactory) {
        this.batchesCount = batchesCount;
        this.textIteratorFactory = textIteratorFactory;
        this.contextFactory = contextFactory;
        this.cellCalculator = cellCalculator;
        this.listFactory = listFactory;
        this.buildingStatisticsFactory = buildingStatisticsFactory;
        this.executor = Executors.newFixedThreadPool(batchesCount);
    }

    @Override
    public IAPTable build(Product[] patternSlp, Product[] textSlp) {
        IConcurrentBuildingStatistics buildingStatistics = buildingStatisticsFactory.create(batchesCount);
        buildingStatistics.begin();
        final IPatternMatchingContext context = contextFactory.create(patternSlp, textSlp);
        final List<BatchContext> batchContexts = new ArrayList<>(batchesCount);
        final int textTotalCount = textSlp.length;
        final int batchSize = (textTotalCount / batchesCount) + 1;
        for (int batchIndex = 0; batchIndex < batchesCount; ++batchIndex) {
            IArithmeticProgressionList progressions = listFactory.create(batchSize);
            ITextIterator textIterator = textIteratorFactory.create(batchIndex, textTotalCount, batchSize, batchesCount);
            batchContexts.add(new BatchContext(progressions, textIterator, buildingStatistics));
        }
        for (int patternIndex = 0; patternIndex < patternSlp.length; ++patternIndex) {
            calculate(context, patternIndex, batchContexts, buildingStatistics);
        }
        buildingStatistics.end();
        return context.getAPTable();
    }


    private void calculate(final IPatternMatchingContext context, final int patternIndex, List<BatchContext> calculatingRows, final IConcurrentBuildingStatistics buildingStatistics) {
        final CountDownLatch done = new CountDownLatch(batchesCount);
        buildingStatistics.beginSplit();
        for (int batchIndex = 0; batchIndex < batchesCount; ++batchIndex) {
            final BatchContext calculatingRow = calculatingRows.get(batchIndex);
            final ITextIterator textIterator = calculatingRow.TextIterator;
            final IArithmeticProgressionList progressions = calculatingRow.Progressions;
            final int currentBatchIndex = batchIndex;
            progressions.reset();
            textIterator.reset();
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    buildingStatistics.beginBatch(currentBatchIndex);
                    while (textIterator.hasNext()) {
                        int textIndex = textIterator.next();
                        ArithmeticProgression progression = cellCalculator.calculate(context, patternIndex, textIndex);
                        progressions.add(progression);
                    }
                    buildingStatistics.endBatch(currentBatchIndex);
                    done.countDown();
                }
            });
        }

        try {
            done.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        buildingStatistics.endSplit();
        buildingStatistics.beginMerge();
        IAPTable apTable = context.getAPTable();
        for (int batchIndex = 0; batchIndex < batchesCount; ++batchIndex) {
            BatchContext calculatingRow = calculatingRows.get(batchIndex);
            IArithmeticProgressionList batchProgressions = calculatingRow.Progressions;
            ITextIterator textIterator = calculatingRow.TextIterator;
            textIterator.reset();
            int size = batchProgressions.size();
            for (int index = 0; index < size; ++index) {
                int textIndex = textIterator.next();
                apTable.set(patternIndex, textIndex, batchProgressions.get(index));
            }
        }
        buildingStatistics.endMerge();
    }

    private static class BatchContext {
        public final IArithmeticProgressionList Progressions;
        public final ITextIterator TextIterator;

        private BatchContext(IArithmeticProgressionList progressions, ITextIterator textIterator, IConcurrentBuildingStatistics buildingStatistics) {
            Progressions = progressions;
            TextIterator = textIterator;
        }
    }
}


