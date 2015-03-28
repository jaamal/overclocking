package patternMatching.fcpm.table.builder;

public interface IPatternMatchingConfig
{
    ExecutionStrategy getExecutionStrategy();

    int getThreadCount();

    APTableType getAPTableType();

    TextsIterationStrategy getTextsIterationStrategy();

    boolean withStatistics();

    LocalSearchStrategy getLocalSearchStrategy();
}
