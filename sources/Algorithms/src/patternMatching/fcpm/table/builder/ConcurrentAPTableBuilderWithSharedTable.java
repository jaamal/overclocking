package patternMatching.fcpm.table.builder;

import patternMatching.fcpm.IPatternMatchingContext;
import patternMatching.fcpm.IPatternMatchingContextFactory;
import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.IAPTableCellCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ConcurrentAPTableBuilderWithSharedTable implements IAPTableBuilder {

    private final int batchesCount;
    private final ITextIteratorFactory textIteratorFactory;
    private final IPatternMatchingContextFactory contextFactory;
    private final ExecutorService executor;
    private final IAPTableCellCalculator cellCalculator;
    private final IConcurrentBuildingStatisticsFactory buildingStatisticsFactory;

    public ConcurrentAPTableBuilderWithSharedTable(int batchesCount, ITextIteratorFactory textIteratorFactory, IPatternMatchingContextFactory contextFactory, IAPTableCellCalculator cellCalculator, IArithmeticProgressionListFactory listFactory, IConcurrentBuildingStatisticsFactory buildingStatisticsFactory) {
        this.batchesCount = batchesCount;
        this.textIteratorFactory = textIteratorFactory;
        this.contextFactory = contextFactory;
        this.cellCalculator = cellCalculator;
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
            ITextIterator textIterator = textIteratorFactory.create(batchIndex, textTotalCount, batchSize, batchesCount);
            batchContexts.add(new BatchContext(textIterator));
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
            final int currentBatchIndex = batchIndex;
            textIterator.reset();
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    buildingStatistics.beginBatch(currentBatchIndex);
                    while (textIterator.hasNext()) {
                        int textIndex = textIterator.next();
                        ArithmeticProgression progression = cellCalculator.calculate(context, patternIndex, textIndex);
                        context.getAPTable().set(patternIndex, textIndex, progression);
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
        buildingStatistics.endMerge();
    }

    private static class BatchContext {
        public final ITextIterator TextIterator;

        private BatchContext(ITextIterator textIterator) {
            TextIterator = textIterator;
        }
    }
}


