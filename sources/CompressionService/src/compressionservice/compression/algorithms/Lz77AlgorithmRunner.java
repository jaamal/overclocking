package compressionservice.compression.algorithms;

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
import compressionservice.compression.algorithms.analysator.IAnalysator;
import compressionservice.compression.algorithms.factorization.IFactorIterator;
import compressionservice.compression.algorithms.factorization.IFactorIteratorFactory;
import compressionservice.compression.parameters.IRunParams;

import dataContracts.FactorDef;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.CompressionStatistics;
import dataContracts.statistics.ICompressionStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class Lz77AlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = LogManager.getLogger(Lz77AlgorithmRunner.class);

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
        try (IReadableCharArray charArray = resourceProvider.getText(runParams)) {
            ITimeCounter timeCounter = new TimeCounter();
            timeCounter.start();

            IFactorIterator factorIterator = factorIteratorFactory.create(runParams, charArray);
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
