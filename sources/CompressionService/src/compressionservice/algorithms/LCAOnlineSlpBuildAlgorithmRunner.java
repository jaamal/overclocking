package compressionservice.algorithms;

import java.util.HashMap;

import org.apache.log4j.Logger;

import storage.IArrayItemsWriter;
import storage.filesRepository.IFilesRepository;
import storage.slpProductsRepository.ISlpProductsRepository;
import SLPs.SlpByteSizeCounter;
import avlTree.slpBuilders.ISLPBuilder;

import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lcaOnlineSlp.ILCAOnlineCompressor;
import compressionservice.runner.parameters.IRunParams;

import dataContracts.DataFactoryType;
import dataContracts.Product;
import dataContracts.SLPStatistics;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class LCAOnlineSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    private static Logger logger = Logger.getLogger(LCAOnlineSlpBuildAlgorithmRunner.class);

    private ILCAOnlineCompressor lcaOnlineCompressor;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private IStatisticsObjectFactory statisticsObjectFactory;
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
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.slpByteSizeCounter = slpByteSizeCounter;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
    }

    @Override
    public StatisticsObject run(IRunParams runParams) {
        try (IReadableCharArray source = resourceProvider.getText(sourceId, dataFactoryType)) {
            logger.info("Source file size is " + source.length());
            TimeCounter timeCounter = TimeCounter.start();
            ISLPBuilder slp = lcaOnlineCompressor.buildSLP(source);
            timeCounter.finish();

            logger.info(String.format("Finish slpBuilding. Total time is about %d minutes", timeCounter.getMillis() / 60 / 1000));
            SLPStatistics slpStatistics = slp.getStatistics();
            HashMap<CompressionStatisticKeys, String> statisitcs = new HashMap<>();
            statisitcs.put(CompressionStatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            statisitcs.put(CompressionStatisticKeys.SlpWidth, String.valueOf(slpStatistics.length));
            statisitcs.put(CompressionStatisticKeys.SlpCountRules, String.valueOf(slpStatistics.countRules));
            statisitcs.put(CompressionStatisticKeys.SlpHeight, String.valueOf(slpStatistics.height));
            statisitcs.put(CompressionStatisticKeys.SlpByteSize, String.valueOf(slpByteSizeCounter.getSlpByteSize(slp)));
            StatisticsObject result = statisticsObjectFactory.create(runParams.toMap(), statisitcs);

            IArrayItemsWriter<Product> writer = slpProductsRepository.getWriter(result.getId());
            Product[] products = slp.toNormalForm();
            writer.addAll(products);
            writer.done();

            return result;
        }
    }
}
