package compressionservice.algorithms;

import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.filesRepository.IFilesRepository;

import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.factorization.IFactorIterator;
import compressionservice.algorithms.factorization.IFactorIteratorFactory;
import compressionservice.profile.IAnalysator;
import compressionservice.runner.parameters.IRunParams;

import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class Lz77AlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = LogManager.getLogger(Lz77AlgorithmRunner.class);

    private final IFactorsRepository factorsRepotisory;
    private final IResourceProvider resourceProvider;
    private final IStatisticsObjectFactory statisticsObjectFactory;
    private final IAnalysator analysator;
    private final IFactorIteratorFactory factorIteratorFactory;

    private String sourceId;
    private DataFactoryType dataFactoryType;
    private int windowSize;

    public Lz77AlgorithmRunner(
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IFactorsRepository factorsRepository,
            IFactorIteratorFactory factorIteratorFactory,
            IAnalysator analysator,
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId, 
            DataFactoryType dataFactoryType,
            int windowSize) {
        this.resourceProvider = resourceProvider;
        this.factorIteratorFactory = factorIteratorFactory;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
        this.windowSize = windowSize;
        this.factorsRepotisory = factorsRepository;
        this.analysator = analysator;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }

    @Override
    public StatisticsObject run(IRunParams runParams) {
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

            IStatistics statistics = new Statistics();
            statistics.putParam(StatisticKeys.SourceLength, String.valueOf(charArray.length()));
            statistics.putParam(StatisticKeys.FactorizationLength, String.valueOf(factors.size()));
            statistics.putParam(StatisticKeys.FactorizationByteSize, String.valueOf(analysator.countByteSize(factors)));
            statistics.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));

            StatisticsObject result = statisticsObjectFactory.create(runParams.toMap(), statistics.toMap());

            IArrayItemsWriter<FactorDef> writer = factorsRepotisory.getWriter(result.getId());
            for (FactorDef factor : factors)
                writer.add(factor);
            writer.done();

            return result;
        }
    }
}
