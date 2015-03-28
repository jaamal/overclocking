package patternMatching.fcpm.table.builder;

public class ConcurrentBuildingStatisticsFactory implements IConcurrentBuildingStatisticsFactory {
    @Override
    public IConcurrentBuildingStatistics create(int batchesCount) {
        return new ConcurrentBuildingStatistics(batchesCount);
    }
}
