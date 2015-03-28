package compressionservice.compression.algorithms;

import commons.utils.ITimeCounter;
import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.analysator.IAnalysator;
import compressionservice.compression.algorithms.lzInf.ILZFactorIterator;
import compressionservice.compression.algorithms.lzInf.ILZFactorIteratorFactory;
import compressionservice.compression.algorithms.lzInf.LZFactor;
import compressionservice.compression.parameters.ICompressionRunParams;
import dataContracts.DataFactoryType;
import dataContracts.LZFactorDef;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.filesRepository.IFilesRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LzInfAlgorithm implements ISlpBuildAlgorithm {

    private static Logger logger = LogManager.getLogger(LzInfAlgorithm.class);

    private final IResourceProvider resourceProvider;
    private final ILZFactorIteratorFactory lzFactorIteratorFactory;
    private final IFilesRepository filesRepository;
    private final IFactorsRepository<LZFactorDef> factorsRepository;
    private final IStatisticsObjectFactory statisticsObjectFactory;
    private final IAnalysator analysator;

    public LzInfAlgorithm(
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            ILZFactorIteratorFactory lzFactorIteratorFactory,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IAnalysator analysator,
            IStatisticsObjectFactory statisticsObjectFactory) {
        this.resourceProvider = resourceProvider;
        this.filesRepository = filesRepository;
        this.lzFactorIteratorFactory = lzFactorIteratorFactory;
        this.factorsRepository = factorsRepositoryFactory.getLZRepository();
        this.analysator = analysator;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }

    @Override
    public StatisticsObject build(ICompressionRunParams runParams) {
        DataFactoryType dataFactoryType = runParams.getEnumValue(DataFactoryType.class, CompressionRunKeys.DataFactoryType);
        try (IReadableCharArray source = resourceProvider.getText(runParams)) {
            ITimeCounter timeCounter = new TimeCounter();
            timeCounter.start();
            ArrayList<LZFactorDef> factors = new ArrayList<>();
            try (ILZFactorIterator factorIterator = lzFactorIteratorFactory.create(dataFactoryType, source)) {
                while (factorIterator.hasFactors()) {
                    if (factors.size() % 10000 == 0)
                        logger.info(String.format("Produced %d factors", factors.size()));

                    LZFactor factor = factorIterator.getNextFactor();
                    long length = factor.endPosition - factor.startPosition;
                    factors.add(new LZFactorDef(factor.isTerminal, factor.startPosition, length, factor.value));
                }
            }
            timeCounter.end();

            HashMap<CompressionStatisticKeys, String> statistics = new HashMap<>();
            statistics.put(CompressionStatisticKeys.SourceLength, String.valueOf(source.length()));
            statistics.put(CompressionStatisticKeys.FactorizationLength, String.valueOf(factors.size()));
            statistics.put(CompressionStatisticKeys.RunningTime, String.valueOf(timeCounter.getTime()));
            statistics.put(CompressionStatisticKeys.FactorizationByteSize, String.valueOf(analysator.countByteSize(factors)));
            StatisticsObject result = statisticsObjectFactory.create(runParams.toMap(), statistics);

            IArrayItemsWriter<LZFactorDef> writer = factorsRepository.getWriter(result.getId());
            for (LZFactorDef factor : factors) {
                writer.add(factor);
            }
            writer.done();

            return result;
        }
    }

    @Override
    public Iterable<String> getAllSourceIds() {
        return Arrays.asList(filesRepository.getAllIds());
    }
}

