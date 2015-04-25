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

public class Worker implements IWorker
{
    private static Logger logger = Logger.getLogger(Worker.class);
    
    private IAlgorithmRunnersFactory algorithmRunnersFactory;
    private IStatisticsRepository statisticsRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;

    public Worker(
            IAlgorithmRunnersFactory algorithmRunnersFactory,
            IStatisticsRepository statisticsRepository,
            IStatisticsObjectFactory statisticsObjectFactory) {
        this.algorithmRunnersFactory = algorithmRunnersFactory;
        this.statisticsRepository = statisticsRepository;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }
    
    
    @Override
    public void process(IRunParams runParams) {
        try {
            ArrayList<Exception> unhandledExceptions = new ArrayList<>();
            
            if (runParams.contains(CompressionRunKeys.SourceId)){
                processItem(runParams.get(CompressionRunKeys.SourceId), runParams, unhandledExceptions);
            }
            else {
                Iterable<String> sourceIds = algorithmRunnersFactory.create(runParams).getAllSourceIds();
                for (String sourceId : sourceIds) {
                    runParams.put(CompressionRunKeys.SourceId, sourceId);
                    processItem(sourceId, runParams, unhandledExceptions);
                }
            }
            
            if (unhandledExceptions.isEmpty())
                logger.info("Done.");
            else
                logger.error(String.format("Done with %d exceptions.", unhandledExceptions.size()));
        } catch (Exception e) {
            logger.error("Runner fails with unhandled exception.", e);
        }
    }

    private void processItem(String sourceId, IRunParams runParams, ArrayList<Exception> unhandledExceptions) {
        try {
            if (statisticsRepository.exists(sourceId, statisticsObjectFactory.getStatisticsObjectId(runParams.toMap()))) {
                logger.info("Skip sourceId = " + sourceId + ", because it was been processed early");
                return;
            }

            logger.info("Begin working on input with id = " + sourceId);
            IAlgorithmRunner algorithmRunner = algorithmRunnersFactory.create(runParams);
            runParams.put(CompressionRunKeys.SourceId, sourceId);
            StatisticsObject statisticsObject = algorithmRunner.run(runParams);
            logger.info("End working on input with id = " + sourceId);

            logger.info("Start saving statistics");
            statisticsRepository.write(sourceId, statisticsObject);
            logger.info("End saving statistics.");
        } catch (Exception e) {
            unhandledExceptions.add(e);
            logger.error("Unhandled exception", e);
        }
        System.gc();
    }
}
