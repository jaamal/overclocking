package patternMatching.fcpm.table.builder;

import patternMatching.fcpm.MultiThreadPatternMatchingContextFactory;
import patternMatching.fcpm.MultiThreadWithSharedTablePatternMatchingContextFactory;
import patternMatching.fcpm.SingleThreadPatternMatchingContextFactory;
import patternMatching.fcpm.localsearch.*;
import patternMatching.fcpm.table.APTableCellCalculator;

public final class APTableBuilderFactory implements IAPTableBuilderFactory {
    public APTableBuilderFactory() {
    }

    @Override
    public IAPTableBuilder create(IPatternMatchingConfig config) {
        ILocalSearchExecutor localSearchExecutor = createLocalSearch(config.getLocalSearchStrategy());
        switch (config.getExecutionStrategy()) {
            case SingleThread:
                return new SimpleAPTableBuilder(new SingleThreadPatternMatchingContextFactory(config), new APTableCellCalculator(localSearchExecutor));
            case MultiThreadWithAggregation: {
                ITextIteratorFactory textIteratorFactory = config.getTextsIterationStrategy() == TextsIterationStrategy.Interval ? new IntervalTextIteratorFactory() : new OrderedTextIteratorFactory();
                IConcurrentBuildingStatisticsFactory buildingStatisticsFactory = config.withStatistics() ? new ConcurrentBuildingStatisticsFactory() : IConcurrentBuildingStatisticsFactory.DummyFactory;
                APTableCellCalculator tableCellCalculator = new APTableCellCalculator(localSearchExecutor);
                return new ConcurrentAPTableBuilder(config.getThreadCount(), textIteratorFactory, new MultiThreadPatternMatchingContextFactory(config), tableCellCalculator, new ArithmeticProgressionListFactory(), buildingStatisticsFactory);

            }
            case MultiThreadWithSharedTable: {
                ITextIteratorFactory textIteratorFactory = config.getTextsIterationStrategy() == TextsIterationStrategy.Interval ? new IntervalTextIteratorFactory() : new OrderedTextIteratorFactory();
                IConcurrentBuildingStatisticsFactory buildingStatisticsFactory = config.withStatistics() ? new ConcurrentBuildingStatisticsFactory() : IConcurrentBuildingStatisticsFactory.DummyFactory;
                APTableCellCalculator tableCellCalculator = new APTableCellCalculator(localSearchExecutor);
                return new ConcurrentAPTableBuilderWithSharedTable(config.getThreadCount(), textIteratorFactory, new MultiThreadWithSharedTablePatternMatchingContextFactory(config), tableCellCalculator, new ArithmeticProgressionListFactory(), buildingStatisticsFactory);
            }
        }
        throw new IllegalStateException();
    }

    private ILocalSearchExecutor createLocalSearch(LocalSearchStrategy strategy) {
        switch (strategy) {
            case Classic:
                return new ClassicLocalSearchExecutor(new LocalSearchResultFactory());
            case Iterative:
                return new IterativeLocalSearchExecutor(new LocalSearchResultFactory());
            case Recursive:
                return new RecursiveLocalSearchExecutor(new LocalSearchResultFactory());
            case RecursiveWithContains:
                return new RecursiveLocalSearchExecutorWithContains(new LocalSearchResultFactory());
        }
        throw new IllegalStateException();
    }
}
