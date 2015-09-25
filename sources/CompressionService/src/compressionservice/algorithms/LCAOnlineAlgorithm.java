package compressionservice.algorithms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;
import commons.utils.TimeCounter;
import compressionservice.algorithms.lcaOnlineSlp.ILCAOnlineCompressor;
import data.charArray.IReadableCharArray;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;
import productEnumerator.IProductEnumerator;
import serialization.products.IProductSerializer;

public class LCAOnlineAlgorithm extends Algorithm implements ISlpCompressionAlgorithm {

    private static Logger logger = Logger.getLogger(LCAOnlineAlgorithm.class);

    private final ILCAOnlineCompressor lcaOnlineCompressor;
    private final IResourceProvider resourceProvider;
    private final IProductSerializer productSerializer;
    private final String sourceId;
    private final DataFactoryType dataFactoryType;
    IStatistics statistics;
    SLPModel slpModel;

    public LCAOnlineAlgorithm(
            ILCAOnlineCompressor lcaOnlineCompressor,
            IResourceProvider resourceProvider,
            IProductSerializer productsSerializer,
            String sourceId,
            DataFactoryType dataFactoryType) {
        this.lcaOnlineCompressor = lcaOnlineCompressor;
        this.resourceProvider = resourceProvider;
        this.productSerializer = productsSerializer;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
    }

    @Override
    protected void runInternal()
    {
        try (IReadableCharArray source = resourceProvider.getText(sourceId, dataFactoryType)) {
            logger.info("Source file size is " + source.length());
            TimeCounter timeCounter = TimeCounter.start();
            IProductEnumerator productEnumerator = lcaOnlineCompressor.buildSLP(source);
            timeCounter.finish();
            logger.info(String.format("Finish slpBuilding. Total time is about %d minutes", timeCounter.getMillis() / 60 / 1000));
            
            slpModel = productEnumerator.toSLPModel();
            statistics = new Statistics();
            statistics.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            slpModel.appendStats(statistics, productSerializer);
        }
    }
    
    @Override
    public IStatistics getStats()
    {
         checkIsFinished();
        return statistics;
    }

    @Override
    public AlgorithmType getType()
    {
        return AlgorithmType.lcaOnlineSlp;
    }

    @Override
    public byte[] getCompressedRepresentation()
    {
        checkIsFinished();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            productSerializer.serialize(stream, getSlp());
            return stream.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException("Fail to build slp compressed representation", e);
        }
    }

    @Override
    public Product[] getSlp()
    {
        checkIsFinished();
        return slpModel.toNormalForm();
    }
}
