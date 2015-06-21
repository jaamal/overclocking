package compressionservice.algorithms;

import org.apache.log4j.Logger;

import serialization.products.IProductsSerializer;
import storage.IArrayItemsWriter;
import storage.filesRepository.IFilesRepository;
import storage.slpProductsRepository.ISlpProductsRepository;
import avlTree.slpBuilders.ISLPBuilder;
import commons.utils.TimeCounter;
import compressionservice.algorithms.lcaOnlineSlp.ILCAOnlineCompressor;
import data.charArray.IReadableCharArray;
import dataContracts.DataFactoryType;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;

public class LCAOnlineSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = Logger.getLogger(LCAOnlineSlpBuildAlgorithmRunner.class);

    private ILCAOnlineCompressor lcaOnlineCompressor;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private String sourceId;
    private DataFactoryType dataFactoryType;
    private final IProductsSerializer productsSerializer;

    public LCAOnlineSlpBuildAlgorithmRunner(
            ILCAOnlineCompressor lcaOnlineCompressor,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IStatisticsObjectFactory statisticsObjectFactory,
            IProductsSerializer productsSerializer,
            String sourceId,
            DataFactoryType dataFactoryType) {
        this.lcaOnlineCompressor = lcaOnlineCompressor;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.productsSerializer = productsSerializer;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
    }

    @Override
    public IStatistics run(String resultId) {
        try (IReadableCharArray source = resourceProvider.getText(sourceId, dataFactoryType)) {
            logger.info("Source file size is " + source.length());
            TimeCounter timeCounter = TimeCounter.start();
            ISLPBuilder slpBuilder = lcaOnlineCompressor.buildSLP(source);
            timeCounter.finish();
            logger.info(String.format("Finish slpBuilding. Total time is about %d minutes", timeCounter.getMillis() / 60 / 1000));
            
            SLPModel slpModel = slpBuilder.toSLPModel();
            IStatistics statisitcs = new Statistics();
            statisitcs.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            slpModel.appendStats(statisitcs, productsSerializer);

            IArrayItemsWriter<Product> writer = slpProductsRepository.getWriter(resultId);
            Product[] products = slpModel.toNormalForm();
            writer.addAll(products);
            writer.done();

            return statisitcs;
        }
    }
}
