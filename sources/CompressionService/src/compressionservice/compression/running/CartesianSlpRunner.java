package compressionservice.compression.running;

import compressionservice.compression.algorithms.IAlgorithmRunnersFactory;
import compressionservice.compression.parameters.IRunParams;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.IStatisticsObjectFactory;
import storage.statistics.IStatisticsRepository;

public class CartesianSlpRunner extends SlpRunner implements ITypedCompressionRunner {

    private final static DataFactoryType DefaultDataFactoryType = DataFactoryType.memory;

    public CartesianSlpRunner(
            IAlgorithmRunnersFactory slpBuildAlgorithmsFactory,
            IStatisticsRepository statisticsRepository,
            IStatisticsObjectFactory statisticsObjectFactory) {
        super(slpBuildAlgorithmsFactory, statisticsRepository, statisticsObjectFactory);
    }

    @Override
    protected CheckParamsResult checkAndRefillParamsInternal(IRunParams runParams) {
        if (!runParams.contains(CompressionRunKeys.DataFactoryType))
            runParams.put(CompressionRunKeys.DataFactoryType, DefaultDataFactoryType);
        return CheckParamsResult.OK;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.cartesianSlp;
    }
}
