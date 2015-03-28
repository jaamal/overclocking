package patternMatching.fcpm.table.builder;

public interface IConcurrentBuildingStatistics {
    void begin();
    void beginSplit();
    void beginBatch(int batchIndex);
    void endBatch(int batchIndex);
    void endSplit();
    void beginMerge();
    void endMerge();
    void end();

    IConcurrentBuildingStatistics DummyStatistics = new IConcurrentBuildingStatistics() {
        @Override
        public void begin() {
        }

        @Override
        public void beginSplit() {
        }

        @Override
        public void beginBatch(int batchIndex) {
        }

        @Override
        public void endBatch(int batchIndex) {
        }

        @Override
        public void endSplit() {
        }

        @Override
        public void beginMerge() {
        }

        @Override
        public void endMerge() {
        }

        @Override
        public void end() {
        }
    };
}

