package compressionservice.algorithms;

import org.apache.log4j.Logger;

import serialization.products.IProductSerializationHeuristic;
import storage.filesRepository.IFilesRepository;
import storage.slpProductsRepository.ISlpProductsRepository;
import commons.utils.TimeCounter;
import compressionservice.algorithms.lcaOnlineSlp.ILCAOnlineCompressor;

import data.charArray.IReadableCharArray;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;
import productEnumerator.IProductEnumerator;

public class LCAOnlineSlpBuildAlgorithmRunner implements IAlgorithm {

    private static Logger logger = Logger.getLogger(LCAOnlineSlpBuildAlgorithmRunner.class);

    private final ILCAOnlineCompressor lcaOnlineCompressor;
    private final ISlpProductsRepository slpProductsRepository;
    private final IResourceProvider resourceProvider;
    private final IProductSerializationHeuristic productsSerializer;
    private final String sourceId;
    private final String resultId;
    private final DataFactoryType dataFactoryType;
    IStatistics statistics;

    public LCAOnlineSlpBuildAlgorithmRunner(
            ILCAOnlineCompressor lcaOnlineCompressor,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IStatisticsObjectFactory statisticsObjectFactory,
            IProductSerializationHeuristic productsSerializer,
            String sourceId,
            String resultId,
            DataFactoryType dataFactoryType) {
        this.lcaOnlineCompressor = lcaOnlineCompressor;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.productsSerializer = productsSerializer;
        this.sourceId = sourceId;
        this.resultId = resultId;
        this.dataFactoryType = dataFactoryType;
    }

    @Override
    public void run() {
        try (IReadableCharArray source = resourceProvider.getText(sourceId, dataFactoryType)) {
            logger.info("Source file size is " + source.length());
            TimeCounter timeCounter = TimeCounter.start();
            IProductEnumerator slpBuilder = lcaOnlineCompressor.buildSLP(source);
            timeCounter.finish();
            logger.info(String.format("Finish slpBuilding. Total time is about %d minutes", timeCounter.getMillis() / 60 / 1000));
            
            SLPModel slpModel = slpBuilder.toSLPModel();
            statistics = new Statistics();
            statistics.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            slpModel.appendStats(statistics, productsSerializer);

            Product[] products = slpModel.toNormalForm();
            slpProductsRepository.writeAll(resultId, products);
        }
    }
    
    @Override
    public IStatistics getStats()
    {
        if (statistics == null)
            throw new RuntimeException("Statistics is empty since algorithm does not running.");
        return statistics;
    }

    @Override
    public AlgorithmType getType()
    {
        return AlgorithmType.lcaOnlineSlp;
    }
}
