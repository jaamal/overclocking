package compressionservice.compression.running;

import dataContracts.statistics.IStatisticsObjectFactory;
import storage.statistics.IStatisticsRepository;

import compressionservice.compression.algorithms.ISlpBuildAlgorithmsFactory;
import compressionservice.compression.parameters.ICompressionRunParams;

import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.statistics.CompressionRunKeys;

public class LCAOnlineRunner extends SlpRunner implements ITypedCompressionRunner {

    private final static DataFactoryType DefaultDataFactoryType = DataFactoryType.memory;

    public LCAOnlineRunner(
            ISlpBuildAlgorithmsFactory slpBuildAlgorithmsFactory,
            IStatisticsRepository statisticsRepository,
            IStatisticsObjectFactory statisticsObjectFactory) {
        super(slpBuildAlgorithmsFactory, statisticsRepository, statisticsObjectFactory);
    }

    @Override
    protected CheckParamsResult checkAndRefillParamsInternal(ICompressionRunParams runParams) {
        if (!runParams.contains(CompressionRunKeys.DataFactoryType))
            runParams.putParam(CompressionRunKeys.DataFactoryType, DefaultDataFactoryType);
        return CheckParamsResult.OK;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.lcaOnlineSlp;
    }
}
