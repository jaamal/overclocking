package compressionservice.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.filesRepository.IFilesRepository;

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
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class LzInfAlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = LogManager.getLogger(LzInfAlgorithmRunner.class);
    
    private final static DataFactoryType defaultDataFactoryType = DataFactoryType.memory;

    private final IResourceProvider resourceProvider;
    private final IFactorIteratorFactory factorIteratorFactory;
    private final IFilesRepository filesRepository;
    private final IFactorsRepository factorsRepository;
    private final IStatisticsObjectFactory statisticsObjectFactory;
    private final IAnalysator analysator;

    public LzInfAlgorithmRunner(
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IFactorIteratorFactory factorIteratorFactory,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IAnalysator analysator,
            IStatisticsObjectFactory statisticsObjectFactory) {
        this.resourceProvider = resourceProvider;
        this.filesRepository = filesRepository;
        this.factorIteratorFactory = factorIteratorFactory;
        this.factorsRepository = factorsRepositoryFactory.getLZRepository();
        this.analysator = analysator;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }

    @Override
    public StatisticsObject run(IRunParams runParams) {
        String sourceId = runParams.get(CompressionRunKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, CompressionRunKeys.DataFactoryType, defaultDataFactoryType);
        
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

            HashMap<CompressionStatisticKeys, String> statistics = new HashMap<>();
            statistics.put(CompressionStatisticKeys.SourceLength, String.valueOf(source.length()));
            statistics.put(CompressionStatisticKeys.FactorizationLength, String.valueOf(factors.size()));
            statistics.put(CompressionStatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            statistics.put(CompressionStatisticKeys.FactorizationByteSize, String.valueOf(analysator.countByteSize(factors)));
            StatisticsObject result = statisticsObjectFactory.create(runParams.toMap(), statistics);

            IArrayItemsWriter<FactorDef> writer = factorsRepository.getWriter(result.getId());
            for (FactorDef factor : factors) {
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

