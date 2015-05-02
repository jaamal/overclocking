package compressionservice.algorithms;

import java.util.ArrayList;
import java.util.HashMap;

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
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class LzInfAlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = LogManager.getLogger(LzInfAlgorithmRunner.class);

    private final IResourceProvider resourceProvider;
    private final IFactorIteratorFactory factorIteratorFactory;
    private final IFactorsRepository factorsRepository;
    private final IStatisticsObjectFactory statisticsObjectFactory;
    private final IAnalysator analysator;
    private String sourceId;
    private DataFactoryType dataFactoryType;

    public LzInfAlgorithmRunner(
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IFactorIteratorFactory factorIteratorFactory,
            IFactorsRepository factorsRepository,
            IAnalysator analysator,
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId,
            DataFactoryType dataFactoryType) {
        this.resourceProvider = resourceProvider;
        this.factorIteratorFactory = factorIteratorFactory;
        this.factorsRepository = factorsRepository;
        this.analysator = analysator;
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
    }

    @Override
    public StatisticsObject run(IRunParams runParams) {
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

            HashMap<StatisticKeys, String> statistics = new HashMap<>();
            statistics.put(StatisticKeys.SourceLength, String.valueOf(source.length()));
            statistics.put(StatisticKeys.FactorizationLength, String.valueOf(factors.size()));
            statistics.put(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            statistics.put(StatisticKeys.FactorizationByteSize, String.valueOf(analysator.countByteSize(factors)));
            StatisticsObject result = statisticsObjectFactory.create(runParams.toMap(), statistics);

            IArrayItemsWriter<FactorDef> writer = factorsRepository.getWriter(result.getId());
            for (FactorDef factor : factors) {
                writer.add(factor);
            }
            writer.done();

            return result;
        }
    }
}

