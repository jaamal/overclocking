package compressionservice.runner;

import java.time.Duration;
import java.util.UUID;

import org.apache.log4j.Logger;

import storage.statistics.IStatisticsRepository;
import commons.utils.TimeCounter;
import compressionservice.algorithms.IAlgorithmRunner;
import compressionservice.algorithms.IAlgorithmRunnersFactory;
import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.state.ITaskOperationalLog;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class Worker implements IWorker
{
    private static Logger logger = Logger.getLogger(Worker.class);
    
    private IAlgorithmRunnersFactory algorithmRunnersFactory;
    private IStatisticsRepository statisticsRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;
    private ITaskOperationalLog operationalLog;

    public Worker(
            IAlgorithmRunnersFactory algorithmRunnersFactory,
            IStatisticsRepository statisticsRepository,
            IStatisticsObjectFactory statisticsObjectFactory,
            ITaskOperationalLog operationalLog) {
        this.algorithmRunnersFactory = algorithmRunnersFactory;
        this.statisticsRepository = statisticsRepository;
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.operationalLog = operationalLog;
    }
    
    @Override
    public void process(UUID requestId, IRunParams runParams) {
        try {
            if (runParams.contains(CompressionRunKeys.SourceId)){
                processItem(requestId, runParams.get(CompressionRunKeys.SourceId), runParams);
            }
            else {
                Iterable<String> sourceIds = algorithmRunnersFactory.create(runParams).getAllSourceIds();
                for (String sourceId : sourceIds) {
                    runParams.put(CompressionRunKeys.SourceId, sourceId);
                    processItem(requestId, sourceId, runParams);
                }
            }
        } catch (Exception e) {
            logger.error("Runner fails with unhandled exception.", e);
        }
    }

    private void processItem(UUID requestId, String sourceId, IRunParams runParams) {
        try {
            operationalLog.append(requestId, sourceId, "Start processing.");
            if (statisticsRepository.exists(sourceId, statisticsObjectFactory.getStatisticsObjectId(runParams.toMap()))) {
                operationalLog.append(requestId, sourceId, "Skip it since it has been already processed.");
                return;
            }

            runParams.put(CompressionRunKeys.SourceId, sourceId);
            IAlgorithmRunner algorithmRunner = algorithmRunnersFactory.create(runParams);
            TimeCounter timeCounter = TimeCounter.start();
            StatisticsObject statisticsObject = algorithmRunner.run(runParams);
            Duration duration = timeCounter.finish();
            statisticsRepository.write(sourceId, statisticsObject);
            
            operationalLog.append(requestId, sourceId, String.format("End processing. Duration: %s.", duration));
        } catch (Exception e) {
            String message = String.format("Process finished with unhandled exception. Message: %s.", e.getMessage());
            operationalLog.append(requestId, sourceId, message);
            logger.error(message, e);
        }
        System.gc();
    }
}
