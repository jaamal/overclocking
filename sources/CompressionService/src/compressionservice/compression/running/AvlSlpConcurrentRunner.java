package compressionservice.compression.running;

import dataContracts.statistics.IStatisticsObjectFactory;
import storage.statistics.IStatisticsRepository;

import compressionservice.compression.algorithms.IAlgorithmRunnersFactory;
import compressionservice.compression.parameters.ICompressionRunParams;

import dataContracts.AlgorithmType;
import dataContracts.AvlMergePattern;
import dataContracts.DataFactoryType;
import dataContracts.statistics.CompressionRunKeys;

public class AvlSlpConcurrentRunner extends SlpRunner implements ITypedCompressionRunner {

    private final static AvlMergePattern DefaultAVLMergePattern = AvlMergePattern.sequential;
    private final static DataFactoryType DefaultDataFactoryType = DataFactoryType.memory;
    private final static int DefaultThreadCount = 4;

    public AvlSlpConcurrentRunner(
            IAlgorithmRunnersFactory slpBuildAlgorithmsFactory,
            IStatisticsRepository statisticsRepository,
            IStatisticsObjectFactory statisticsObjectFactory) {
        super(slpBuildAlgorithmsFactory, statisticsRepository, statisticsObjectFactory);
    }

    @Override
    protected CheckParamsResult checkAndRefillParamsInternal(ICompressionRunParams runParams) {
        if (!runParams.contains(CompressionRunKeys.AvlMergePattern))
            runParams.putParam(CompressionRunKeys.AvlMergePattern, DefaultAVLMergePattern);
        if (!runParams.contains(CompressionRunKeys.DataFactoryType))
            runParams.putParam(CompressionRunKeys.DataFactoryType, DefaultDataFactoryType);
        if (!runParams.contains(CompressionRunKeys.ThreadCount))
            runParams.putParam(CompressionRunKeys.ThreadCount, DefaultThreadCount);
        return CheckParamsResult.OK;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.avlSlpConcurrent;
    }
}
