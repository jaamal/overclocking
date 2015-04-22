package compressionservice.compression.running;

import dataContracts.statistics.IStatisticsObjectFactory;
import storage.statistics.IStatisticsRepository;

import compressionservice.compression.algorithms.IAlgorithmRunnersFactory;
import compressionservice.compression.parameters.IRunParams;

import dataContracts.AlgorithmType;
import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.statistics.CompressionRunKeys;

public class AvlSlpRunner extends SlpRunner implements ITypedCompressionRunner {

    private final static AvlMergePattern DefaultAvlMergePattern = AvlMergePattern.block;
    private final static AvlSplitPattern DefaultAvlSplitPattern = AvlSplitPattern.fromMerged;
    private final static DataFactoryType DefaultDataFactoryType = DataFactoryType.memory;

    public AvlSlpRunner(
            IAlgorithmRunnersFactory slpBuildAlgorithmsFactory,
            IStatisticsRepository statisticsRepository,
            IStatisticsObjectFactory statisticsObjectFactory) {
        super(slpBuildAlgorithmsFactory, statisticsRepository, statisticsObjectFactory);
    }

    @Override
    protected CheckParamsResult checkAndRefillParamsInternal(IRunParams runParams) {
        if (!runParams.contains(CompressionRunKeys.AvlMergePattern))
            runParams.put(CompressionRunKeys.AvlMergePattern, DefaultAvlMergePattern);
        if (!runParams.contains(CompressionRunKeys.AvlSplitPattern))
            runParams.put(CompressionRunKeys.AvlSplitPattern, DefaultAvlSplitPattern);
        if (!runParams.contains(CompressionRunKeys.DataFactoryType))
            runParams.put(CompressionRunKeys.DataFactoryType, DefaultDataFactoryType);
        return CheckParamsResult.OK;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.avlSlp;
    }
}
