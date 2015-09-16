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

public class Lz77AlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = LogManager.getLogger(Lz77AlgorithmRunner.class);

    private final IFactorsRepository factorsRepotisory;
    private final IResourceProvider resourceProvider;
    private final IFactorIteratorFactory factorIteratorFactory;

    private final String sourceId;
    private final String resultId;
    private final DataFactoryType dataFactoryType;
    private final int windowSize;
    private IStatistics statistics;

    public Lz77AlgorithmRunner(
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IFactorsRepository factorsRepository,
            IFactorIteratorFactory factorIteratorFactory,
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId, 
            String resultId,
            DataFactoryType dataFactoryType,
            int windowSize) {
        this.resourceProvider = resourceProvider;
        this.factorIteratorFactory = factorIteratorFactory;
        this.sourceId = sourceId;
        this.resultId = resultId;
        this.dataFactoryType = dataFactoryType;
        this.windowSize = windowSize;
        this.factorsRepotisory = factorsRepository;
    }

    @Override
    public void run() {
        try (IReadableCharArray charArray = resourceProvider.getText(sourceId, dataFactoryType)) {
            TimeCounter timeCounter = TimeCounter.start();

            IFactorIterator factorIterator = factorIteratorFactory.createWindowIterator(charArray, windowSize);
            ArrayList<FactorDef> factors = new ArrayList<>();
            while (factorIterator.any()) {
                if (factors.size() % 10000 == 0)
                    logger.info(String.format("Produced %d factors", factors.size()));
                factors.add(factorIterator.next());
            }
            timeCounter.finish();

            statistics = new Statistics();
            statistics.putParam(StatisticKeys.SourceLength, String.valueOf(charArray.length()));
            statistics.putParam(StatisticKeys.FactorizationLength, String.valueOf(factors.size()));
            statistics.putParam(StatisticKeys.FactorizationByteSize, String.valueOf(FactorDef.SIZE_IN_BYTES * factors.size()));
            statistics.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));

            factorsRepotisory.writeAll(resultId, factors.toArray(new FactorDef[0]));
        }
    }
    
    @Override
    public IStatistics getStats()
    {
        if (statistics == null)
            throw new RuntimeException("Statistics is empty since algorithm does not running.");
        return statistics;
    }
}
