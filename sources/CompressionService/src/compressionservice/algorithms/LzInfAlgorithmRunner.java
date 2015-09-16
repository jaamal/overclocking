package compressionservice.algorithms;

import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import storage.factorsRepository.IFactorsRepository;
import storage.filesRepository.IFilesRepository;

import commons.utils.TimeCounter;
import compressionservice.algorithms.factorization.IFactorIterator;
import compressionservice.algorithms.factorization.IFactorIteratorFactory;

import data.charArray.IReadableCharArray;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;

public class LzInfAlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = LogManager.getLogger(LzInfAlgorithmRunner.class);

    private final IResourceProvider resourceProvider;
    private final IFactorIteratorFactory factorIteratorFactory;
    private final IFactorsRepository factorsRepository;
    private String sourceId;
    private String resultId;
    private DataFactoryType dataFactoryType;

    public LzInfAlgorithmRunner(
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IFactorIteratorFactory factorIteratorFactory,
            IFactorsRepository factorsRepository,
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId,
            String resultId,
            DataFactoryType dataFactoryType) {
        this.resourceProvider = resourceProvider;
        this.factorIteratorFactory = factorIteratorFactory;
        this.factorsRepository = factorsRepository;
        this.sourceId = sourceId;
        this.resultId = resultId;
        this.dataFactoryType = dataFactoryType;
    }

    @Override
    public IStatistics run() {
        try (IReadableCharArray source = resourceProvider.getText(sourceId, dataFactoryType)) {
            TimeCounter timeCounter = TimeCounter.start();
            ArrayList<FactorDef> factors = new ArrayList<>();
            try (IFactorIterator factorIterator = factorIteratorFactory.createInfiniteIterator(source, dataFactoryType)) {
                while (factorIterator.any()) {
                    if (factors.size() % 10000 == 0)
                        logger.info(String.format("Produced %d factors", factors.size()));

                    FactorDef factor = factorIterator.next();
                    factors.add(factor);
                }
            } catch (Exception e) {
                logger.error(String.format("Fail to run lzInf algorithm."), e);
            }
            timeCounter.finish();

            IStatistics statistics = new Statistics();
            statistics.putParam(StatisticKeys.SourceLength, String.valueOf(source.length()));
            statistics.putParam(StatisticKeys.FactorizationLength, String.valueOf(factors.size()));
            statistics.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            statistics.putParam(StatisticKeys.FactorizationByteSize, String.valueOf(FactorDef.SIZE_IN_BYTES * factors.size()));

            factorsRepository.writeAll(resultId, factors.toArray(new FactorDef[0]));
            return statistics;
        }
    }
}

