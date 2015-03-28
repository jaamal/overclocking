package patternMatching.fcpm.table.builder;

public interface IConcurrentBuildingStatisticsFactory {
    IConcurrentBuildingStatisticsFactory DummyFactory = new IConcurrentBuildingStatisticsFactory() {
        @Override
        public IConcurrentBuildingStatistics create(int batchesCount) {
            return IConcurrentBuildingStatistics.DummyStatistics;
        }
    };

    IConcurrentBuildingStatistics create(int batchesCount);
}
