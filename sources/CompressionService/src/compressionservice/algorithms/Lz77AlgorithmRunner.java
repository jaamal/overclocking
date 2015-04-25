package compressionservice.algorithms;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.filesRepository.IFilesRepository;
import commons.utils.ITimeCounter;
import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.factorization.IFactorIterator;
import compressionservice.algorithms.factorization.IFactorIteratorFactory;
import compressionservice.profile.IAnalysator;
import compressionservice.runner.parameters.IRunParams;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.CompressionStatistics;
import dataContracts.statistics.ICompressionStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class Lz77AlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = LogManager.getLogger(Lz77AlgorithmRunner.class);
    
    private final static DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
    private final static int defaultWindowSize = 32 * 1024;

    private final IFactorsRepository factorsRepotisory;
    private final IResourceProvider resourceProvider;
    private final IStatisticsObjectFactory statisticsObjectFactory;
    private final IFilesRepository filesRepository;
    private final IAnalysator analysator;
    private final IFactorIteratorFactory factorIteratorFactory;

    public Lz77AlgorithmRunner(
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IFactorIteratorFactory factorIteratorFactory,
            IAnalysator analysator,
            IStatisticsObjectFactory statisticsObjectFactory) {
        this.resourceProvider = resourceProvider;
        this.filesRepository = filesRepository;
        this.factorIteratorFactory = factorIteratorFactory;
        this.factorsRepotisory = factorsRepositoryFactory.getLZ77Repository();
        this.analysator = analysator;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }

    @Override
    public StatisticsObject run(IRunParams runParams) {
        String sourceId = runParams.get(CompressionRunKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, CompressionRunKeys.DataFactoryType, defaultDataFactoryType);
        int windowSize = runParams.getOrDefaultInt(CompressionRunKeys.WindowSize, defaultWindowSize);
        
        try (IReadableCharArray charArray = resourceProvider.getText(sourceId, dataFactoryType)) {
            ITimeCounter timeCounter = new TimeCounter();
            timeCounter.start();

            IFactorIterator factorIterator = factorIteratorFactory.createWindowIterator(charArray, windowSize);
            ArrayList<FactorDef> factors = new ArrayList<>();
            while (factorIterator.any()) {
                if (factors.size() % 10000 == 0)
                    logger.info(String.format("Produced %d factors", factors.size()));
                factors.add(factorIterator.next());
            }
            timeCounter.end();

            ICompressionStatistics statistics = new CompressionStatistics();
            statistics.putParam(CompressionStatisticKeys.SourceLength, String.valueOf(charArray.length()));
            statistics.putParam(CompressionStatisticKeys.FactorizationLength, String.valueOf(factors.size()));
            statistics.putParam(CompressionStatisticKeys.FactorizationByteSize, String.valueOf(analysator.countByteSize(factors)));
            statistics.putParam(CompressionStatisticKeys.RunningTime, String.valueOf(timeCounter.getTime()));

            StatisticsObject result = statisticsObjectFactory.create(runParams.toMap(), statistics.toMap());

            IArrayItemsWriter<FactorDef> writer = factorsRepotisory.getWriter(result.getId());
            for (FactorDef factor : factors)
                writer.add(factor);
            writer.done();

            return result;
        }
    }

    @Override
    public Iterable<String> getAllSourceIds() {
        return Arrays.asList(filesRepository.getAllIds());
    }
}
