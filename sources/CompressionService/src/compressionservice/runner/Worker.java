package compressionservice.runner;

import java.time.Duration;
import java.util.UUID;
import org.apache.log4j.Logger;
import commons.utils.TimeCounter;
import compressionservice.algorithms.IAlgorithmRunner;
import compressionservice.algorithms.IAlgorithmRunnersFactory;
import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.state.ITaskOperationalLog;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.RunParamKeys;
import storage.statistics.IStatisticsRepository;

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
            if (runParams.contains(RunParamKeys.SourceId)){
                processItem(requestId, runParams.get(RunParamKeys.SourceId), runParams);
            }
            else {
                Iterable<String> sourceIds = algorithmRunnersFactory.getAllSourceIds((runParams));
                for (String sourceId : sourceIds) {
                    runParams.put(RunParamKeys.SourceId, sourceId);
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
            String resultId = runParams.getHashId();
            if (statisticsRepository.exists(sourceId, resultId)) {
                operationalLog.append(requestId, sourceId, "Skip it since it has been already processed.");
                return;
            }

            runParams.put(RunParamKeys.ResultId, resultId);
            IAlgorithmRunner algorithmRunner = algorithmRunnersFactory.create(runParams);
            TimeCounter timeCounter = TimeCounter.start();
            algorithmRunner.run();
            Duration duration = timeCounter.finish();
            statisticsRepository.write(sourceId, statisticsObjectFactory.create(resultId, runParams.toMap(), algorithmRunner.getStats().toMap()));
            
            operationalLog.append(requestId, sourceId, String.format("End processing. Duration: %s.", duration));
        } catch (Exception e) {
            String message = String.format("Process finished with unhandled exception. Message: %s.", e.getMessage());
            operationalLog.append(requestId, sourceId, message);
            logger.error(message, e);
        }
        System.gc();
    }
}
