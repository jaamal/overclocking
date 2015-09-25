package compressionservice.runner;

import java.time.Duration;
import java.util.UUID;
import org.apache.log4j.Logger;
import commons.utils.TimeCounter;
import compressionservice.algorithms.IAlgorithm;
import compressionservice.algorithms.IAlgorithmsFactory;
import compressionservice.algorithms.IFactorizationCompressionAlgorithm;
import compressionservice.algorithms.ISlpCompressionAlgorithm;
import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.state.ITaskOperationalLog;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.RunParamKeys;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import storage.statistics.IStatisticsRepository;

public class Worker implements IWorker
{
    private static Logger logger = Logger.getLogger(Worker.class);
    
    private IAlgorithmsFactory algorithmRunnersFactory;
    private IStatisticsRepository statisticsRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;
    private ITaskOperationalLog operationalLog;
    private IFactorsRepositoryFactory factorsRepositoryFactory;
    private ISlpProductsRepository slpProductsRepository;

    public Worker(
            IAlgorithmsFactory algorithmRunnersFactory,
            IStatisticsRepository statisticsRepository,
            IStatisticsObjectFactory statisticsObjectFactory,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            ISlpProductsRepository slpProductsRepository,
            ITaskOperationalLog operationalLog) {
        this.algorithmRunnersFactory = algorithmRunnersFactory;
        this.statisticsRepository = statisticsRepository;
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.factorsRepositoryFactory = factorsRepositoryFactory;
        this.slpProductsRepository = slpProductsRepository;
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
            IAlgorithm algorithm = algorithmRunnersFactory.create(runParams);
            TimeCounter timeCounter = TimeCounter.start();
            algorithm.run();
            Duration duration = timeCounter.finish();

            if (algorithm instanceof IFactorizationCompressionAlgorithm) {
                postprocessRun(sourceId, runParams, (IFactorizationCompressionAlgorithm) algorithm);
            }
            if (algorithm instanceof ISlpCompressionAlgorithm) {
                postprocessRun(sourceId, runParams, (ISlpCompressionAlgorithm) algorithm);
            }
            else {
                postprocessRun(sourceId, runParams, algorithm);
            }
            
            operationalLog.append(requestId, sourceId, String.format("End processing. Duration: %s.", duration));
        } catch (Exception e) {
            String message = String.format("Process finished with unhandled exception. Message: %s.", e.getMessage());
            operationalLog.append(requestId, sourceId, message);
            logger.error(message, e);
        }
        System.gc();
    }
    
    private void postprocessRun(String sourceId, IRunParams runParams, IFactorizationCompressionAlgorithm algorithm) {
        String resultId = runParams.getHashId();
        statisticsRepository.write(sourceId, statisticsObjectFactory.create(resultId, runParams.toMap(), algorithm.getStats().toMap()));
        if (algorithm.supportFactorization()) {
            IFactorsRepository factorsRepository = factorsRepositoryFactory.find(algorithm.getType());
            if (factorsRepository != null) {
                factorsRepository.writeAll(resultId, algorithm.getFactorization());
            }
        }
    }
    
    private void postprocessRun(String sourceId, IRunParams runParams, ISlpCompressionAlgorithm algorithm) {
        String resultId = runParams.getHashId();
        statisticsRepository.write(sourceId, statisticsObjectFactory.create(resultId, runParams.toMap(), algorithm.getStats().toMap()));
        slpProductsRepository.writeAll(resultId, algorithm.getSlp());
    }
    
    private void postprocessRun(String sourceId, IRunParams runParams, IAlgorithm algorithm) {
        statisticsRepository.write(sourceId, statisticsObjectFactory.create(runParams.getHashId(), runParams.toMap(), algorithm.getStats().toMap()));
    }
}
