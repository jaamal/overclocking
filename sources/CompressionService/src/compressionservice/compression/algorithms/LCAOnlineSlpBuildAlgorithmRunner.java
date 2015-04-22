package compressionservice.compression.algorithms;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;

import avlTree.slpBuilders.ISLPBuilder;
import storage.IArrayItemsWriter;
import storage.filesRepository.IFilesRepository;
import storage.slpProductsRepository.ISlpProductsRepository;
import SLPs.SlpByteSizeCounter;
import commons.utils.ITimeCounter;
import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lcaOnlineSlp.ILCAOnlineCompressor;
import compressionservice.compression.parameters.IRunParams;
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
    private IFilesRepository filesRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;
    private final SlpByteSizeCounter slpByteSizeCounter;

    public LCAOnlineSlpBuildAlgorithmRunner(
            ILCAOnlineCompressor lcaOnlineCompressor,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository,
            IStatisticsObjectFactory statisticsObjectFactory,
            SlpByteSizeCounter slpByteSizeCounter) {
        this.lcaOnlineCompressor = lcaOnlineCompressor;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.filesRepository = filesRepository;
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.slpByteSizeCounter = slpByteSizeCounter;
    }

    @Override
    public StatisticsObject run(IRunParams runParams) {
        try (IReadableCharArray source = resourceProvider.getText(runParams)) {
            logger.info("Source file size is " + source.length());
            ITimeCounter timeCounter = new TimeCounter();
            timeCounter.start();
            ISLPBuilder slp = lcaOnlineCompressor.buildSLP(source);
            timeCounter.end();

            logger.info(String.format("Finish slpBuilding. Total time is about %d minutes", timeCounter.getTime() / 60 / 1000));
            SLPStatistics slpStatistics = slp.getStatistics();
            HashMap<CompressionStatisticKeys, String> statisitcs = new HashMap<>();
            statisitcs.put(CompressionStatisticKeys.RunningTime, String.valueOf(timeCounter.getTime()));
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

    @Override
    public Iterable<String> getAllSourceIds() {
        return Arrays.asList(filesRepository.getAllIds());
    }

}
