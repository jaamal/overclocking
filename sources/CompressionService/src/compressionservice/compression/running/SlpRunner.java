package compressionservice.compression.running;

import compressionservice.compression.algorithms.ISlpBuildAlgorithm;
import compressionservice.compression.algorithms.ISlpBuildAlgorithmsFactory;
import compressionservice.compression.parameters.ICompressionRunParams;
import dataContracts.AlgorithmType;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;
import org.apache.log4j.Logger;
import storage.statistics.IStatisticsRepository;

import java.util.ArrayList;

public abstract class SlpRunner implements ITypedCompressionRunner {
    private static Logger logger = Logger.getLogger(SlpRunner.class);

    private final ISlpBuildAlgorithmsFactory buildAlgorithmsFactory;
    private final IStatisticsRepository statisticsRepository;
    private final IStatisticsObjectFactory statisticsObjectFactory;

    protected SlpRunner(ISlpBuildAlgorithmsFactory buildAlgorithmsFactory,
                        IStatisticsRepository statisticsRepository,
                        IStatisticsObjectFactory statisticsObjectFactory) {
        this.buildAlgorithmsFactory = buildAlgorithmsFactory;
        this.statisticsRepository = statisticsRepository;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }

    @Override
    public void run(ICompressionRunParams runParams) {
        try {
            ISlpBuildAlgorithm buildAlgorithm = buildAlgorithmsFactory.create(runParams);
            Iterable<String> sourceIds = buildAlgorithm.getAllSourceIds();
            ArrayList<Exception> unhandledExceptions = new ArrayList<>();
            for (String sourceId : sourceIds) {
                try {
                    runParams.putParam(CompressionRunKeys.SourceId, sourceId);

                    if (statisticsRepository.exists(sourceId, statisticsObjectFactory.getStatisticsObjectId(runParams.toMap()))) {
                        logger.info("Skip sourceId = " + sourceId + ", because it was been processed early");
                        continue;
                    }

                    logger.info("Start building slp on source with id = " + sourceId);
                    StatisticsObject statisticsObject = buildAlgorithm.build(runParams);
                    logger.info("End building.");

                    logger.info("Start saving statistics");
                    statisticsRepository.write(sourceId, statisticsObject);
                    logger.info("Statistics saved.");
                } catch (Exception e) {
                    unhandledExceptions.add(e);
                    logger.error("Unhandled exception", e);
                }
                System.gc();
            }
            if (unhandledExceptions.isEmpty())
                logger.info("Done.");
            else
                logger.error(String.format("Done with %d exceptions.", unhandledExceptions.size()));
        } catch (Exception e) {
            logger.error(String.format("'%s' fails with unhandled exception.", this.getClass().getName()), e);
        }
    }

    @Override
    public CheckParamsResult checkAndRefillParams(ICompressionRunParams runParams) {
        if (runParams.contains(CompressionRunKeys.AlgorithmType)) {
            AlgorithmType currentAlgorithmType = runParams.getEnumValue(AlgorithmType.class, CompressionRunKeys.AlgorithmType);
            if (currentAlgorithmType != getAlgorithmType()) {
                String message = String.format("Parameter '%s' equals to '%s', but it must be equal to '%s'", CompressionRunKeys.AlgorithmType, currentAlgorithmType.name(), getAlgorithmType().name());
                return CheckParamsResult.failed(message);
            }
        } else
            runParams.putParam(CompressionRunKeys.AlgorithmType, getAlgorithmType());

        return checkAndRefillParamsInternal(runParams);
    }

    protected abstract CheckParamsResult checkAndRefillParamsInternal(ICompressionRunParams runParams);
}
