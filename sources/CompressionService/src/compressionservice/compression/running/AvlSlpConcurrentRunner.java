package compressionservice.compression.running;

import storage.statistics.IStatisticsRepository;

import compressionservice.compression.algorithms.IAlgorithmRunnersFactory;

import dataContracts.AlgorithmType;
import dataContracts.statistics.IStatisticsObjectFactory;

public class AvlSlpConcurrentRunner extends SlpRunner implements ITypedCompressionRunner {

    public AvlSlpConcurrentRunner(
            IAlgorithmRunnersFactory slpBuildAlgorithmsFactory,
            IStatisticsRepository statisticsRepository,
            IStatisticsObjectFactory statisticsObjectFactory) {
        super(slpBuildAlgorithmsFactory, statisticsRepository, statisticsObjectFactory);
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.avlSlpConcurrent;
    }
}
