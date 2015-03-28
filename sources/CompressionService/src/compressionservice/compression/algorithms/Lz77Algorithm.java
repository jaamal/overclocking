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
import compressionservice.compression.algorithms.lz77.LZ77FactorIterator;
import compressionservice.compression.algorithms.lz77.windows.IWindowFactory;
import compressionservice.compression.parameters.ICompressionRunParams;

import dataContracts.FactorDef;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.CompressionStatistics;
import dataContracts.statistics.ICompressionStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class Lz77Algorithm implements ISlpBuildAlgorithm {

    private static Logger logger = LogManager.getLogger(Lz77Algorithm.class);

    private final IFactorsRepository<FactorDef> factorsRepotisory;
    private final IResourceProvider resourceProvider;
    private final IWindowFactory windowFactory;
    private final IStatisticsObjectFactory statisticsObjectFactory;
    private final IFilesRepository filesRepository;
    private final IAnalysator analysator;

    public Lz77Algorithm(
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IWindowFactory windowFactory,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IAnalysator analysator,
            IStatisticsObjectFactory statisticsObjectFactory) {
        this.resourceProvider = resourceProvider;
        this.filesRepository = filesRepository;
        this.windowFactory = windowFactory;
        this.factorsRepotisory = factorsRepositoryFactory.getLZ77Repository();
        this.analysator = analysator;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }

    @Override
    public StatisticsObject build(ICompressionRunParams runParams) {
        try (IReadableCharArray charArray = resourceProvider.getText(runParams)) {
            ITimeCounter timeCounter = new TimeCounter();
            timeCounter.start();

            int windowSize = runParams.getIntValue(CompressionRunKeys.WindowSize);
            LZ77FactorIterator lz77FactorIterator = new LZ77FactorIterator(windowFactory, charArray, windowSize);
            ArrayList<FactorDef> factors = new ArrayList<>();
            while (lz77FactorIterator.hasFactors()) {
                if (factors.size() % 10000 == 0)
                    logger.info(String.format("Produced %d factors", factors.size()));

                factors.add(lz77FactorIterator.getNextFactor());
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
