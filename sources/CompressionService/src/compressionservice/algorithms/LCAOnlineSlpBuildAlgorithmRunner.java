package compressionservice.algorithms;

import org.apache.log4j.Logger;

import storage.IArrayItemsWriter;
import storage.filesRepository.IFilesRepository;
import storage.slpProductsRepository.ISlpProductsRepository;
import SLPs.SlpByteSizeCounter;
import avlTree.slpBuilders.ISLPBuilder;
import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lcaOnlineSlp.ILCAOnlineCompressor;
import dataContracts.DataFactoryType;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.SLPStatistics;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;

public class LCAOnlineSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = Logger.getLogger(LCAOnlineSlpBuildAlgorithmRunner.class);

    private ILCAOnlineCompressor lcaOnlineCompressor;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private final SlpByteSizeCounter slpByteSizeCounter;
    private String sourceId;
    private DataFactoryType dataFactoryType;

    public LCAOnlineSlpBuildAlgorithmRunner(
            ILCAOnlineCompressor lcaOnlineCompressor,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IStatisticsObjectFactory statisticsObjectFactory,
            SlpByteSizeCounter slpByteSizeCounter,
            String sourceId,
            DataFactoryType dataFactoryType) {
        this.lcaOnlineCompressor = lcaOnlineCompressor;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.slpByteSizeCounter = slpByteSizeCounter;
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
            SLPStatistics slpStatistics = slpModel.calcStats();
            IStatistics statisitcs = new Statistics();
            statisitcs.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            statisitcs.putParam(StatisticKeys.SlpWidth, String.valueOf(slpStatistics.length));
            statisitcs.putParam(StatisticKeys.SlpCountRules, String.valueOf(slpStatistics.countRules));
            statisitcs.putParam(StatisticKeys.SlpHeight, String.valueOf(slpStatistics.height));
            statisitcs.putParam(StatisticKeys.SlpByteSize, String.valueOf(slpByteSizeCounter.getSlpByteSize(slpModel)));

            IArrayItemsWriter<Product> writer = slpProductsRepository.getWriter(resultId);
            Product[] products = slpModel.toNormalForm();
            writer.addAll(products);
            writer.done();

            return statisitcs;
        }
    }
}
