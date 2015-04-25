package compressionservice.compression.running;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import storage.statistics.IStatisticsRepository;

import compressionservice.compression.algorithms.IAlgorithmRunner;
import compressionservice.compression.algorithms.IAlgorithmRunnersFactory;
import compressionservice.compression.parameters.IRunParams;

import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public abstract class SlpRunner implements ITypedCompressionRunner {
    private static Logger logger = Logger.getLogger(SlpRunner.class);

    private final IAlgorithmRunnersFactory buildAlgorithmsFactory;
    private final IStatisticsRepository statisticsRepository;
    private final IStatisticsObjectFactory statisticsObjectFactory;

    protected SlpRunner(IAlgorithmRunnersFactory buildAlgorithmsFactory,
                        IStatisticsRepository statisticsRepository,
                        IStatisticsObjectFactory statisticsObjectFactory) {
        this.buildAlgorithmsFactory = buildAlgorithmsFactory;
        this.statisticsRepository = statisticsRepository;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }

    @Override
    public void run(IRunParams runParams) {
        try {
            IAlgorithmRunner buildAlgorithm = buildAlgorithmsFactory.create(runParams);
            ArrayList<Exception> unhandledExceptions = new ArrayList<>();
            
            if (runParams.contains(CompressionRunKeys.SourceId)){
                run(buildAlgorithm, unhandledExceptions, runParams);
            }
            else {
                Iterable<String> sourceIds = buildAlgorithm.getAllSourceIds();
                for (String sourceId : sourceIds) {
                    runParams.put(CompressionRunKeys.SourceId, sourceId);
                    run(buildAlgorithm, unhandledExceptions, runParams);
                }
            }
            
            if (unhandledExceptions.isEmpty())
                logger.info("Done.");
            else
                logger.error(String.format("Done with %d exceptions.", unhandledExceptions.size()));
        } catch (Exception e) {
            logger.error(String.format("'%s' fails with unhandled exception.", this.getClass().getName()), e);
        }
    }
    
    private void run(IAlgorithmRunner buildAlgorithm, ArrayList<Exception> unhandledExceptions, IRunParams runParams) {
        try {
            String sourceId = runParams.get(CompressionRunKeys.SourceId);
            if (statisticsRepository.exists(sourceId, statisticsObjectFactory.getStatisticsObjectId(runParams.toMap()))) {
                logger.info("Skip sourceId = " + sourceId + ", because it was been processed early");
                return;
            }

            logger.info("Start building slp on source with id = " + sourceId);
            StatisticsObject statisticsObject = buildAlgorithm.run(runParams);
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
}
