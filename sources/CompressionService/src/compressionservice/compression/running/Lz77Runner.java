package compressionservice.compression.running;

import storage.statistics.IStatisticsRepository;

import compressionservice.compression.algorithms.IAlgorithmRunnersFactory;

import dataContracts.AlgorithmType;
import dataContracts.statistics.IStatisticsObjectFactory;

public class Lz77Runner extends SlpRunner implements ITypedCompressionRunner {
    
    public Lz77Runner(
            IAlgorithmRunnersFactory slpBuildAlgorithmsFactory,
            IStatisticsRepository statisticsRepository,
            IStatisticsObjectFactory statisticsObjectFactory) {
        super(slpBuildAlgorithmsFactory, statisticsRepository, statisticsObjectFactory);
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.lz77;
    }
}
